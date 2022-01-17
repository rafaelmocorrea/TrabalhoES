package com.esgrupo10.SATM.controller;

import com.esgrupo10.SATM.entity.Medico;
import com.esgrupo10.SATM.entity.Paciente;
import com.esgrupo10.SATM.repository.MedicoRepository;
import com.esgrupo10.SATM.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class SatmController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping("/")
    public String viewHP() {
        return "index";
    }

    @GetMapping("/soupac")
    public String souPac() {
        return "soupac";
    }

    @GetMapping("/soumed")
    public String souMed() {
        return "soumed";
    }

    @GetMapping("/cadastrapac")
    public String cadastroPac(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "cadastropac";
    }

    @PostMapping("/paciente_registro")
    public String pacienteRegistro(Paciente pac) {
        BCryptPasswordEncoder encript = new BCryptPasswordEncoder();
        String senha_cript = encript.encode(pac.getSenha());
        pac.setSenha(senha_cript);

        try {
            pacienteRepository.save(pac);
        } catch (DataIntegrityViolationException e) {
            return "falha";
        }
        return "paciente_sucesso";
    }

    @GetMapping("/cadastramed")
    public String cadastroMed(Model model) {
        model.addAttribute("medico", new Medico());
        return "cadastromed";
    }

    @PostMapping("/medico_registro")
    public String medicoRegistro(Medico med) {
        BCryptPasswordEncoder encript = new BCryptPasswordEncoder();
        String senha_cript = encript.encode(med.getSenha());
        med.setSenha(senha_cript);

        try {
            medicoRepository.save(med);
        } catch (DataIntegrityViolationException e) {
            return "falha";
        }
        return "medico_sucesso";
    }

    @GetMapping("/sucesso")
    public String sucesso() {
        return "sucesso";
    }

    @GetMapping("/menupaciente")
    public String menuPaciente() {
        return "menupaciente";
    }

    @GetMapping("/menumedico")
    public String menuMedico() {
        return "menumedico";
    }

}
