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

    public Exame getExame(Long ID) {
        return exameRepository.findById(ID).get();
    }

    public List<Exame> getExames() {
        return exameRepository.findAll();
    }

    public String criaExame(Exame exame) {
        try {
            exameRepository.save(exame);
        } catch (Exception e) {
            return "falha";
        }

        return "receita_sucesso";
    }

    public void updateExame(Exame exame) {
        exameRepository.save(exame);
    }

    public List<Exame> listaExamesMedico(Medico m) {
        return exameRepository.findAllWithMedic(m);
    }

    public List<Exame> listaExamesPaciente(Paciente p) {
        return exameRepository.findAllWithPatient(p);
    }

    public List<Exame> listaExamesPacienteMedico(Paciente p, Medico m) {
        return exameRepository.findAllWithPatientAndMedic(p,m);
    }

    public List<Exame> listaExamesMedicoBoolean(Medico m, Boolean b) {
        return exameRepository.findAllDoneWithMedic(m,b);
    }

    public void apagaExame(Exame exame) {
        exameRepository.delete(exame);
    }

    public List<Exame> listaExamesPacienteBoolean(Paciente p, Boolean b) {
        return exameRepository.findAllDoneWithPatient(p,b);
    }
}
