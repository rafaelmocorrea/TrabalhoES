package com.esgrupo10.SATM.controller;

import com.esgrupo10.SATM.details.PacienteDetails;
import com.esgrupo10.SATM.model.PedidoConsulta;
import com.esgrupo10.SATM.service.PacienteService;
import com.esgrupo10.SATM.service.PedidoConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PedidoController {

    @Autowired
    PedidoConsultaService pedidoConsultaService;

    @Autowired
    PacienteService pacienteService;

    @GetMapping("/pedidoconsulta")
    public String cadastraConsulta(Model model) {
        model.addAttribute("pedidoconsulta", new PedidoConsulta());
        return "cadastracons";
    }

    @PostMapping("/pedido_registro")
    public String pedidoConsultaRegistro(PedidoConsulta ped) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        ped.setPaciente(pacienteService.encontraPorEmail(username));
        ped.setAtendida(Boolean.FALSE);
        return pedidoConsultaService.criaPedido(ped);
    }

}