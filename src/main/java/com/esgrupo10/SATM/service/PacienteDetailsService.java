package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.entity.Paciente;
import com.esgrupo10.SATM.entity.PacienteDetails;
import com.esgrupo10.SATM.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PacienteDetailsService implements UserDetailsService {

    @Autowired
    private PacienteRepository pacRep;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Paciente pac = pacRep.findbyEmail(username);
        if (pac == null) {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        }
        return new PacienteDetails(pac);
    }
}
