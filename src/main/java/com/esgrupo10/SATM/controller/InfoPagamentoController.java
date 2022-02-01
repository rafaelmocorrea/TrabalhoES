package com.esgrupo10.SATM.controller;

import com.esgrupo10.SATM.details.PacienteDetails;
import com.esgrupo10.SATM.model.InfoPagamento;
import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.service.InfoPagamentoService;
import com.esgrupo10.SATM.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class InfoPagamentoController {

    @Autowired
    InfoPagamentoService infoPagamentoService;

    @Autowired
    PacienteService pacienteService;

    @GetMapping("/menupaciente/metodos/")
    public String administraMetodos(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        List<InfoPagamento> metodos = infoPagamentoService.metodosPagamentoPaciente(pac);
        model.addAttribute("metodos",metodos);

        return "administrametodos";
    }

    @GetMapping("/menupaciente/apagametodo/{metid}")
    public String apagaMetodo(@PathVariable Long metid) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        InfoPagamento inf = infoPagamentoService.encontraInfoPagamento(metid);
        if (pac.getId() != inf.getPaciente().getId()) {
            return "falha";
        }
        infoPagamentoService.deletaMetodo(inf);

        return "menupaciente";
    }

    @GetMapping("/menupaciente/adicionametodo/")
    public String adicionaMetodo(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        InfoPagamento infoPagamento = new InfoPagamento();
        infoPagamento.setPaciente(pac);
        model.addAttribute("metodo",infoPagamento);

        return "cadastro_metodo";
    }

    @PostMapping("/menupaciente/addmetodo/")
    public String addMetodo(InfoPagamento info) {
        return infoPagamentoService.adicionarMetodoPagamento(info);
    }

}
