package com.esgrupo10.SATM.repository;

import com.esgrupo10.SATM.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    @Query("SELECT u FROM Paciente u WHERE u.email = ?1")
    public Paciente findbyEmail(String email);
}
