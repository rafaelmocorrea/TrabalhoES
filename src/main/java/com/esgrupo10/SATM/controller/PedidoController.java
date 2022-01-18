package com.esgrupo10.SATM.controller;

import com.esgrupo10.SATM.details.PacienteDetails;
import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.model.PedidoConsulta;
import com.esgrupo10.SATM.service.PacienteService;
import com.esgrupo10.SATM.service.PedidoConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PedidoController {

    @Autowired
    PedidoConsultaService pedidoConsultaService;

    @Autowired
    PacienteService pacienteService;

    @GetMapping("/menupaciente/pedidoconsulta")
    public String cadastraConsulta(Model model) {
        model.addAttribute("pedidoconsulta", new PedidoConsulta());
        return "cadastracons";
    }

    @PostMapping("/menupaciente/pedido_registro")
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

    @GetMapping("/menupaciente/gerenciarpedidos")
    public String listaPedidos(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        List<PedidoConsulta> pedidos = pedidoConsultaService.listaPedidosUsuario(pac);

        model.addAttribute("pedidos",pedidos);
        return "listapedidos";
    }

    @GetMapping("/menupaciente/deletaconsulta/{consId}")
    public String deletaConsulta(@PathVariable Long consId) {
        pedidoConsultaService.deletaPedido(pedidoConsultaService.getPedido(consId));
        return "pedidodeletado";
    }

}
