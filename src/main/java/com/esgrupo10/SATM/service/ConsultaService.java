package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.model.Consulta;
import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    ConsultaRepository consultaRepository;

    public String criaConsulta(Consulta consulta) {
        try {
            consultaRepository.save(consulta);
        } catch (Exception e) {
            return "falha";
        }

        return "consulta_sucesso";
    }

    public List<Consulta> listaConsultas() {
        return consultaRepository.findAll();
    }

    public void deletaConsulta(Consulta consulta) {
        consultaRepository.delete(consulta);
    }

    public List<Consulta> listaConsultasUsuario(Paciente p) {
        return consultaRepository.findAllWithPatient(p);
    }

    public List<Consulta> listaConsultasMedico(Medico m)  {
        return consultaRepository.findAllWithMedic(m);
    }

}
