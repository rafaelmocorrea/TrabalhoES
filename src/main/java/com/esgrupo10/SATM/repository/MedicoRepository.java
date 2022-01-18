package com.esgrupo10.SATM.repository;

import com.esgrupo10.SATM.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    @Query("SELECT u FROM Medico u WHERE u.email = ?1")
    public Medico findbyEmail(String email);
}
