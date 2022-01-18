package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.details.PacienteDetails;
import com.esgrupo10.SATM.model.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PacienteDetailsService implements UserDetailsService {

    @Autowired
    private PacienteService pacienteService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Paciente pac = pacienteService.encontraPorEmail(username);
        if (pac == null) {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        }
        return new PacienteDetails(pac);
    }
}
