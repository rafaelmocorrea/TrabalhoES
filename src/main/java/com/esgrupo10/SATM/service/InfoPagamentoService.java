package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.model.InfoPagamento;
import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.repository.InfoPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoPagamentoService {

    @Autowired
    InfoPagamentoRepository infoPagamentoRepository;

    public String adicionarMetodoPagamento(InfoPagamento infoPagamento) {
        try {
            infoPagamentoRepository.save(infoPagamento);
        } catch (Exception e) {
            return "falha";
        }

        return "menupaciente";
    }

    public InfoPagamento encontraInfoPagamento(Long id) {
        return infoPagamentoRepository.findById(id).get();
    }

    public List<InfoPagamento> metodosPagamentoPaciente(Paciente p) {
        return infoPagamentoRepository.findByPatient(p);
    }

    public void deletaMetodo(InfoPagamento infoPagamento) {
        infoPagamentoRepository.delete(infoPagamento);
    }

}
