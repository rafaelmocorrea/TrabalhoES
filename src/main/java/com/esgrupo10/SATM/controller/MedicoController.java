package com.esgrupo10.SATM.controller;

import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/soumed")
    public String souMed() {
        return "soumed";
    }

    @GetMapping("/cadastramed")
    public String cadastroMed(Model model) {
        model.addAttribute("medico", new Medico());
        return "cadastromed";
    }

    @PostMapping("/medico_registro")
    public String medicoRegistro(Medico med) {
        return medicoService.registraMedico(med);
    }

    @GetMapping("/menumedico")
    public String menuMedico() {
        return "menumedico";
    }

}
