package com.esgrupo10.SATM.repository;

import com.esgrupo10.SATM.model.Consulta;
import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    @Query("SELECT u FROM Consulta u WHERE u.paciente = ?1")
    public List<Consulta> findAllWithPatient(Paciente p);

    @Query("SELECT u FROM Consulta u WHERE u.medico = ?1")
    public List<Consulta> findAllWithMedic(Medico m);

    @Query("SELECT u FROM Consulta u WHERE u.paciente = ?1 AND u.data = ?2")
    List<Consulta> findDailyPatient(Paciente p, java.sql.Date data);

    @Query("SELECT u FROM Consulta u WHERE u.medico = ?1 AND u.data = ?2")
    List<Consulta> findDailyMedic(Medico m, java.sql.Date data);
}
