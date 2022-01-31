package com.esgrupo10.SATM.controller;

import com.esgrupo10.SATM.DTO.ExameDTO;
import com.esgrupo10.SATM.details.MedicoDetails;
import com.esgrupo10.SATM.model.Consulta;
import com.esgrupo10.SATM.model.Exame;
import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.model.Paciente;
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
public class ExameController {

    @Autowired
    private ExameService exameService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/menumedico/reqexame")
    public String requisitaExame(Model model) {
        ExameDTO exameDTO = new ExameDTO();
        exameDTO.setPaciente_email("");
        exameDTO.setNome("");
        exameDTO.setDescricao("");
        exameDTO.setData(new java.sql.Date(System.currentTimeMillis()));
        model.addAttribute("exameDTO",exameDTO);

        return "requisitaexame";
    }

    @PostMapping(value = "/menumedico/atualizalink/{consid}",params = "exame")
    public String reqExamePac(Model model, @PathVariable Long consid) {
        Consulta cons = consultaService.getConsulta(consid);
        ExameDTO exameDTO = new ExameDTO();
        exameDTO.setPaciente_email(cons.getPaciente().getEmail());
        exameDTO.setNome("");
        exameDTO.setDescricao("");
        exameDTO.setData(cons.getData());
        model.addAttribute("exameDTO",exameDTO);

        return "requisitaexame";
    }

    @PostMapping("/menumedico/confirma_exame")
    public String confirmaExame(ExameDTO exameDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        Paciente pac = pacienteService.encontraPorEmail(exameDTO.getPaciente_email());
        Exame exame = new Exame();
        exame.setMedico(med);
        exame.setPaciente(pac);
        exame.setData(exameDTO.getData());
        exame.setNome(exameDTO.getNome());
        exame.setDescricao(exameDTO.getDescricao());
        exame.setFeito(Boolean.FALSE);

        String retorno = exameService.criaExame(exame);

        if (retorno.equals("receita_sucesso")) {
            notificacaoService.notificaExame(pac,med,exame.getNome());
        }

        return retorno;
    }

    @GetMapping("/menumedico/acessarexames")
    public String acessarExame(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        List<Exame> exames = exameService.listaExamesMedico(med);
        model.addAttribute("exames",exames);

        return "listaexames";
    }

    @GetMapping("/menumedico/exame/{exid}")
    public String veExame(Model model, @PathVariable Long exid) {
        Exame exame = exameService.getExame(exid);
        model.addAttribute("exame",exame);

        return "exibeexame";
    }

    @GetMapping("/menumedico/apagaexame/{exid}")
    public String apagaExame(@PathVariable Long exid) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        Exame exame = exameService.getExame(exid);
        if (exame.getFeito() == Boolean.TRUE) {
            return "falha_feito";
        } else if (!exame.getMedico().getCpf().equals(med.getCpf())) {
            return "falha_med";
        }

        exameService.apagaExame(exame);
        notificacaoService.notificaExameApagado(exame.getPaciente(),med,exame.getNome());

        return "menumedico";
    }

}
