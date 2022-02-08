package com.esgrupo10.SATM.controller;

import com.esgrupo10.SATM.DTO.PedidoDTO;
import com.esgrupo10.SATM.details.MedicoDetails;
import com.esgrupo10.SATM.details.PacienteDetails;
import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.model.PedidoConsulta;
import com.esgrupo10.SATM.service.MedicoService;
import com.esgrupo10.SATM.service.PacienteService;
import com.esgrupo10.SATM.service.PedidoConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    MedicoService medicoService;

    @GetMapping("/menupaciente/pedidoconsulta")
    public String cadastraPedido(Model model) {
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

    @GetMapping("/menumedico/verpedidos")
    public String mostraPedidos(Model model) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        List<PedidoConsulta> pedidos = pedidoConsultaService.listaTodosComEspecialidade(med.getEspecialidade());

        model.addAttribute("pedidos",pedidos);

        return "verpedidos";
    }

    @PostMapping(value = "/menumedico/verpedidos/filtro",params = "espec")
    public String pedidosEspecializados(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        List<PedidoConsulta> pedidos = pedidoConsultaService.listaTodosComEspecialidade(med.getEspecialidade());

        model.addAttribute("pedidos",pedidos);

        return "verpedidos";
    }

    @PostMapping(value = "/menumedico/verpedidos/filtro",params = "todos")
    public String todosPedidos(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        List<PedidoConsulta> pedidos = pedidoConsultaService.listaPedidos();

        model.addAttribute("pedidos",pedidos);

        return "verpedidos";
    }

    @GetMapping("/menumedico/pedido/{pedid}")
    public String verpedido(Model model, @PathVariable Long pedid) {
        PedidoConsulta ped = pedidoConsultaService.getPedido(pedid);
        Paciente pac = ped.getPaciente();
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setPedido_id(ped.getId());
        pedidoDTO.setDescricao(ped.getDescricao());
        pedidoDTO.setPaciente_nome(pac.getNome());
        pedidoDTO.setPaciente_cpf(pac.getCpf());
        pedidoDTO.setPaciente_id(pac.getId());
        model.addAttribute("pedidoDTO",pedidoDTO);

        return "verpedido";
    }
}
