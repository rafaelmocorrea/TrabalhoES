package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.model.Exame;
import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.repository.ExameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class ExameService {

    @Autowired
    private ExameRepository exameRepository;

    public Exame saveFile(MultipartFile file, String nomeEx, String descr, java.sql.Date data, Boolean feito, Paciente pac, Medico med) {
        String nome = file.getOriginalFilename();
        try {
            Exame exame = new Exame();
            exame.setNome(nomeEx);
            exame.setDescricao(descr);
            exame.setData(data);
            exame.setFeito(feito);
            exame.setPaciente(pac);
            exame.setMedico(med);
            exame.setDocName(nome);
            exame.setDocType(file.getContentType());
            exame.setDados(file.getBytes());

            return exameRepository.save(exame);
        } catch (Exception e) {
            System.out.println("Erro");
            return null;
        }
    }

    public Exame getExame(Long ID) {
        return exameRepository.findById(ID).get();
    }

    public List<Exame> getExames() {
        return exameRepository.findAll();
    }
}
