package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    @Autowired
    PacienteRepository pacienteRepository;

    public String registraPaciente(Paciente pac) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senha_cript = encoder.encode(pac.getSenha());
        pac.setSenha(senha_cript);

        try {
            pacienteRepository.save(pac);
        } catch (DataIntegrityViolationException e) {
            return "falha";
        }

        return "paciente_sucesso";
    }

    public Paciente encontraPorEmail(String email) {
        return pacienteRepository.findbyEmail(email);
    }

    public Paciente encontraPorId(Long ID) {
        return pacienteRepository.findById(ID).get();
    }

    public void atualizaPaciente(Paciente paciente) {
        pacienteRepository.save(paciente);
    }
}
