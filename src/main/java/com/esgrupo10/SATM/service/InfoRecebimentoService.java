package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.model.InfoRecebimento;
import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.repository.InfoRecebimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoRecebimentoService {

    @Autowired
    InfoRecebimentoRepository infoRecebimentoRepository;

    public String adicionarMetodoRecebimento(InfoRecebimento infoRecebimento) {
        try {
            infoRecebimentoRepository.save(infoRecebimento);
        } catch (Exception e) {
            return "falha";
        }
        return "menumedico";
    }

    public InfoRecebimento encontraInfoRecebimento(Long id) {
        return infoRecebimentoRepository.findById(id).get();
    }

    public List<InfoRecebimento> metodosRecebimentoMedico(Medico m) {
        return infoRecebimentoRepository.findByMedic(m);
    }

    public void deletaMetodo(InfoRecebimento infoRecebimento) {
        infoRecebimentoRepository.delete(infoRecebimento);
    }

}
