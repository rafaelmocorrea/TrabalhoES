package com.esgrupo10.SATM.controller;

import com.esgrupo10.SATM.DTO.ConsultaDTO;
import com.esgrupo10.SATM.details.MedicoDetails;
import com.esgrupo10.SATM.model.Consulta;
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
public class ConsultaController {

    @Autowired
    ConsultaService consultaService;

    @Autowired
    PacienteService pacienteService;

    @Autowired
    MedicoService medicoService;

    @Autowired
    PedidoConsultaService pedidoConsultaService;

    @Autowired
    NotificacaoService notificacaoService;

    @GetMapping("/menumedico/aceitapedido/{pedid}")
    public String aceitaPedido(Model model, @PathVariable Long pedid){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        ConsultaDTO consultaDTO = new ConsultaDTO();
        consultaDTO.setPedido_id(pedid);
        consultaDTO.setPaciente_email(pedidoConsultaService.getPedido(pedid).getPaciente().getEmail());
        consultaDTO.setMedico_email(username);
        consultaDTO.setDescricao(pedidoConsultaService.getPedido(pedid).getDescricao());

        model.addAttribute("consultaDTO",consultaDTO);
        return "aceitaconsulta";
    }

    @PostMapping("/menumedico/aceita_consulta")
    public String confirmaPedido(ConsultaDTO consultaDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        Paciente pac = pedidoConsultaService.getPedido(consultaDTO.getPedido_id()).getPaciente();
        System.out.println(pac.getEmail() + "   "+ med.getEmail()+ "  "+consultaDTO.getValor()+ "   AQUI<<<<<");
        Consulta consulta = new Consulta();
        consulta.setPaciente(pac);
        consulta.setMedico(med);
        consulta.setData(consultaDTO.getData());
        consulta.setValor(consultaDTO.getValor());
        consulta.setDescricao(consultaDTO.getDescricao());
        consulta.setStatus("Marcada");

        String retorno = consultaService.criaConsulta(consulta);
        if (retorno.equals("consulta_sucesso")) {
            pedidoConsultaService.deletaPedido(pedidoConsultaService.getPedido(consultaDTO.getPedido_id()));
        }

        return retorno;
    }

    @GetMapping("/menumedico/agendaconsulta")
    public String agendaConsulta(Model model) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        ConsultaDTO consultaDTO = new ConsultaDTO();
        consultaDTO.setMedico_email(username);

        model.addAttribute("consultaDTO",consultaDTO);

        return "abre_consulta";
    }

    @PostMapping("/menumedico/confirma_consulta")
    public String confirmaConsulta(ConsultaDTO consultaDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        Paciente pac = pacienteService.encontraPorEmail(consultaDTO.getPaciente_email());

        Consulta consulta = new Consulta();
        consulta.setMedico(med);
        consulta.setPaciente(pac);
        consulta.setDescricao(consultaDTO.getDescricao());
        consulta.setData(consultaDTO.getData());
        consulta.setValor(consultaDTO.getValor());
        consulta.setStatus("Marcada");

        notificacaoService.notificaConsulta(pac,med,consulta.getData());

        return consultaService.criaConsulta(consulta);
    }

    @GetMapping("/menumedico/abrirconsulta")
    public String abrirConsulta(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        java.sql.Date data = new java.sql.Date(System.currentTimeMillis());
        List<Consulta> consultas = consultaService.listaConsultasDiaMedico(med,data);

        System.out.println("Medico: "+med.getNome()+"Data: "+data);

        model.addAttribute("consultas",consultas);

        return "abreconsultas";
    }

    @GetMapping("/menumedico/abre/{consid}")
    public String abreConsulta(Model model, @PathVariable Long consid) {
        Consulta cons = consultaService.getConsulta(consid);
        model.addAttribute("consulta",cons);

        return "insere_link";
    }

    @PostMapping("/menumedico/inserelink")
    public String atualizaLink(Consulta consulta) {
        consulta.setStatus("Em andamento");
        consultaService.updateConsulta(consulta);
        notificacaoService.notificaLink(consulta.getPaciente(),consulta.getLinkvideoconf());

        return "sucesso_link";
    }
}
