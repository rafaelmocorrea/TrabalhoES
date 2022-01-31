package com.esgrupo10.SATM.repository;

import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    @Query("SELECT u FROM Receita u WHERE u.paciente = ?1")
    public List<Receita> findAllWithPatient(Paciente p);

}
