package com.esgrupo10.SATM.details;

import com.esgrupo10.SATM.model.Medico;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MedicoDetails implements UserDetails {

    private Medico medico;

    public MedicoDetails(Medico med) {
        this.medico = med;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority("MEDICO"));
        list.add(new SimpleGrantedAuthority("PACIENTE"));

        return list;
    }

    @Override
    public String getPassword() {
        return medico.getSenha();
    }

    @Override
    public String getUsername() {
        return medico.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
