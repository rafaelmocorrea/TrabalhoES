package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.details.MedicoDetails;
import com.esgrupo10.SATM.model.Medico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MedicoDetailsService implements UserDetailsService {

    @Autowired
    MedicoService medicoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Medico med = medicoService.encontraPorEmail(username);
        if (med == null) {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        }

        return new MedicoDetails(med);

    }
}
