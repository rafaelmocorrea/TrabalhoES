package com.esgrupo10.SATM.repository;

import com.esgrupo10.SATM.model.InfoRecebimento;
import com.esgrupo10.SATM.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InfoRecebimentoRepository extends JpaRepository<InfoRecebimento, Long> {

    @Query("SELECT u FROM InfoRecebimento u WHERE u.medico = ?1")
    List<InfoRecebimento> findByMedic(Medico m);
}
