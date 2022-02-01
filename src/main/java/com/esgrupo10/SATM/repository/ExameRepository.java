package com.esgrupo10.SATM.repository;

import com.esgrupo10.SATM.model.Exame;
import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExameRepository extends JpaRepository<Exame, Long> {

    @Query("SELECT u FROM Exame u WHERE u.paciente = ?1 ORDER BY u.data")
    public List<Exame> findAllWithPatient(Paciente p);

    @Query("SELECT u FROM Exame u WHERE u.medico = ?1 ORDER BY u.data")
    public List<Exame> findAllWithMedic(Medico m);

    @Query("SELECT u FROM Exame u WHERE u.medico = ?1 AND u.feito = ?2 ORDER BY u.data")
    public List<Exame> findAllDoneWithMedic(Medico m,Boolean b);

    @Query("SELECT u FROM Exame u WHERE u.paciente = ?1 AND u.feito = ?2 ORDER BY u.data")
    List<Exame> findAllDoneWithPatient(Paciente p, Boolean b);

    @Query("SELECT u FROM Exame u WHERE u.paciente = ?1 AND u.medico = ?2 ORDER BY u.data")
    List<Exame> findAllWithPatientAndMedic(Paciente p, Medico m);
}
