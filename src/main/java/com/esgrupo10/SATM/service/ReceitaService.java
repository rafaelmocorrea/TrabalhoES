package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.model.Receita;
import com.esgrupo10.SATM.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    public String criaReceita(Receita receita) {
        try {
            receitaRepository.save(receita);
        } catch (Exception e) {
            return "falha";
        }

        return "receita_sucesso";
    }

    public void deletaReceita(Receita receita) {
        receitaRepository.delete(receita);
    }

    public List<Receita> listaReceitasPac(Paciente p) {
        return receitaRepository.findAllWithPatient(p);
    }
}
