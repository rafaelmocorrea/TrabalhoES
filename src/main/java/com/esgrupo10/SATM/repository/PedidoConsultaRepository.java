package com.esgrupo10.SATM.repository;

import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.model.PedidoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoConsultaRepository extends JpaRepository<PedidoConsulta, Long> {

    @Query(value = "SELECT u FROM PedidoConsulta u WHERE u.paciente = ?1")
    public List<PedidoConsulta> findAllWithID(Paciente p);

    @Query("SELECT u FROM PedidoConsulta u WHERE u.descricao = ?1 AND u.atendida=False")
    public List<PedidoConsulta> findAllWithE(String s);

    @Query("SELECT u FROM PedidoConsulta u WHERE u.atendida=False")
    public List<PedidoConsulta> findAllAvailable();
}
