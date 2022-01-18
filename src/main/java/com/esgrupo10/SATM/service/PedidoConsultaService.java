package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.model.PedidoConsulta;
import com.esgrupo10.SATM.repository.PedidoConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoConsultaService {

    @Autowired
    PedidoConsultaRepository pedidoConsultaRepository;

    public String criaPedido(PedidoConsulta pedidoConsulta) {
        try {
            pedidoConsultaRepository.save(pedidoConsulta);
        } catch (Exception e) {
            return "falha";
        }

        return "pedido_sucesso";
    }

    public List<PedidoConsulta> listaPedidos() {
        return pedidoConsultaRepository.findAll();
    }

    public void deletaPedido(PedidoConsulta pedidoConsulta) {
        pedidoConsultaRepository.delete(pedidoConsulta);
    }

    public PedidoConsulta getPedido(Long ID) {
        return pedidoConsultaRepository.findById(ID).get();
    }

    public List<PedidoConsulta> listaPedidosUsuario(Paciente p) {
        return pedidoConsultaRepository.findAllWithID(p);
    }

    public List<PedidoConsulta> listaTodosComEspecialidade(String e) {
        return pedidoConsultaRepository.findAllWithE(e);
    }

}
