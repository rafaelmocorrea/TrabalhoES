package com.esgrupo10.SATM.controller;

import com.esgrupo10.SATM.details.PacienteDetails;
import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/soupac")
    public String souPac() {
        return "soupac";
    }

    @GetMapping("/cadastrapac")
    public String cadastroPac(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "cadastropac";
    }

    @PostMapping("/paciente_registro")
    public String pacienteRegistro(Paciente pac) {
        return pacienteService.registraPaciente(pac);
    }

    @GetMapping("/menupaciente")
    public String menuPaciente() {
        return "menupaciente";
    }

    @GetMapping("/menumedico/paciente/{pacid}")
    public String exibePaciente(Model model, @PathVariable Long pacid) {
        Paciente pac = pacienteService.encontraPorId(pacid);
        model.addAttribute("pac",pac);

        return "mostrapaciente";
    }

    @GetMapping("/menupaciente/minhaconta")
    public String minhaConta(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        model.addAttribute("paciente",pac);

        return "administracontapac";
    }

    @PostMapping("/menupaciente/atualizaconta")
    public String atualizaConta(Paciente paciente) {
        pacienteService.atualizaPaciente(paciente);

        return "menupaciente";
    }

}
