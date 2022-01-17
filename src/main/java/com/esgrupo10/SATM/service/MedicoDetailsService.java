package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.entity.Medico;
import com.esgrupo10.SATM.entity.MedicoDetails;
import com.esgrupo10.SATM.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MedicoDetailsService implements UserDetailsService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Medico med = medicoRepository.findbyEmail(username);

        if (med == null) {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        }

        return new MedicoDetails(med);

    }
}
