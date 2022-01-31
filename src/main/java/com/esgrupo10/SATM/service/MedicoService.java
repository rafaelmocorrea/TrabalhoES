package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MedicoService {

    @Autowired
    MedicoRepository medicoRepository;

    public String registraMedico(Medico med) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senha_cript = encoder.encode(med.getSenha());
        med.setSenha(senha_cript);

        try {
            medicoRepository.save(med);
        } catch (DataIntegrityViolationException e) {
            return "falha";
        }

        return "medico_sucesso";
    }

    public Medico encontraPorEmail(String email) {
        return medicoRepository.findbyEmail(email);
    }

    public Medico encontraPorId(Long ID) {
        return medicoRepository.findById(ID).get();
    }

    public void atualizaMedico(Medico med) {
        medicoRepository.save(med);
    }

}
