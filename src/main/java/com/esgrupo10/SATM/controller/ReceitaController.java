package com.esgrupo10.SATM.controller;

import com.esgrupo10.SATM.DTO.ReceitaDTO;
import com.esgrupo10.SATM.details.MedicoDetails;
import com.esgrupo10.SATM.details.PacienteDetails;
import com.esgrupo10.SATM.model.Consulta;
import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.model.Receita;
import com.esgrupo10.SATM.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/menumedico/precrreceita")
    public String prescreveReceita(Model model) {
        ReceitaDTO receitaDTO = new ReceitaDTO();
        receitaDTO.setPaciente_email("");
        receitaDTO.setRemedios("");
        receitaDTO.setData(new java.sql.Date(System.currentTimeMillis()));
        model.addAttribute("receitaDTO",receitaDTO);

        return "prescrevereceita";
    }

    @PostMapping("/menumedico/confirma_receita")
    public String confirmaReceita(ReceitaDTO receitaDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        Paciente pac = pacienteService.encontraPorEmail(receitaDTO.getPaciente_email());
        Receita receita = new Receita();
        receita.setData(receitaDTO.getData());
        receita.setRemedios(receitaDTO.getRemedios());
        receita.setMedico(med);
        receita.setPaciente(pac);
        String retorno = receitaService.criaReceita(receita);
        if (retorno.equals("receita_sucesso")) {
            notificacaoService.notificaReceita(pac,med);
        }
        return retorno;
    }

    @PostMapping(value = "/menumedico/atualizalink/{consid}",params = "receita")
    public String prescreveRecPac(Model model, @PathVariable Long consid) {
        Consulta cons = consultaService.getConsulta(consid);
        ReceitaDTO receitaDTO = new ReceitaDTO();
        receitaDTO.setData(cons.getData());
        receitaDTO.setPaciente_email(cons.getPaciente().getEmail());
        receitaDTO.setRemedios("");
        model.addAttribute("receitaDTO",receitaDTO);

        return "prescrevereceita";
    }

    @GetMapping("/menupaciente/gerenciarreceitas")
    public String gerenciaReceitas(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        List<Receita> receitas = receitaService.listaReceitasPac(pac);
        model.addAttribute("receitas",receitas);

        return "gerenciareceitas";
    }

    @GetMapping("/menupaciente/receita/{recid}")
    public String exibeReceita(Model model, @PathVariable Long recid) {
        Receita rec = receitaService.encontraPorId(recid);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        model.addAttribute("receita",rec);
        if (!rec.getPaciente().getEmail().equals(username)) {
            return "403";
        }
        return "mostrareceita";
    }

    @GetMapping("/menupaciente/apagareceita/{recid}")
    public String apagaReceita(@PathVariable Long recid) {
        Receita rec = receitaService.encontraPorId(recid);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        if (!rec.getPaciente().getEmail().equals(username)) {
            return "403";
        }
        receitaService.deletaReceita(rec);

        return "menupaciente";
    }

}
