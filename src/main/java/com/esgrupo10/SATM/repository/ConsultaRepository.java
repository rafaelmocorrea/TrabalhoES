package com.esgrupo10.SATM.repository;

import com.esgrupo10.SATM.model.Consulta;
import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    @Query("SELECT u FROM Consulta u WHERE u.paciente = ?1 ORDER BY u.data")
    public List<Consulta> findAllWithPatient(Paciente p);

    @Query("SELECT u FROM Consulta u WHERE u.medico = ?1 ORDER BY u.data")
    public List<Consulta> findAllWithMedic(Medico m);

    @Query("SELECT u FROM Consulta u WHERE u.paciente = ?1 AND u.data = ?2 ORDER BY u.data")
    List<Consulta> findDailyPatient(Paciente p, java.sql.Date data);

    @Query("SELECT u FROM Consulta u WHERE u.medico = ?1 AND u.data = ?2 ORDER BY u.data")
    List<Consulta> findDailyMedic(Medico m, java.sql.Date data);

    @Query("SELECT u FROM Consulta u WHERE u.medico = ?1 AND u.status = ?2 ORDER BY u.data")
    List<Consulta> findStatusMedic(Medico m, String s);

    @Query("SELECT u FROM Consulta u WHERE u.medico = ?1 AND u.status <> ?2 ORDER BY u.data")
    List<Consulta> findNotStatusMedic(Medico m, String s);

    @Query("SELECT u FROM Consulta u WHERE u.paciente = ?1 AND u.status = ?2 ORDER BY u.data")
    List<Consulta> findStatusPatient(Paciente p, String s);

    @Query("SELECT u FROM Consulta u WHERE u.paciente = ?1 AND u.status <> ?2  ORDER BY u.data")
    List<Consulta> findNotStatusPatient(Paciente p, String s);

    @Query("SELECT u FROM Consulta u WHERE u.medico = ?1 AND u.status <> ?2 AND u.data < ?3")
    List<Consulta> findNotStatusPreDateMedic(Medico m, String s, java.sql.Date data);

}
