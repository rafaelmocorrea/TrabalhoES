package com.esgrupo10.SATM.controller;

import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

}