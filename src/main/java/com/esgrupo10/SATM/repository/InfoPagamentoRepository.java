package com.esgrupo10.SATM.repository;

import com.esgrupo10.SATM.model.InfoPagamento;
import com.esgrupo10.SATM.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InfoPagamentoRepository extends JpaRepository<InfoPagamento, Long> {

    @Query("SELECT u FROM InfoPagamento u WHERE u.paciente = ?1")
    List<InfoPagamento> findByPatient(Paciente p);

}
