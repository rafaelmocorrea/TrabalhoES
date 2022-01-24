package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.DTO.ConsultaDTO;
import com.esgrupo10.SATM.model.Consulta;
import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Consulta> listaConsultasDiaUsuario(Paciente p,java.sql.Date data) { return consultaRepository.findDailyPatient(p,data);}

    public List<Consulta> listaConsultasDiaMedico(Medico m,java.sql.Date data) { return consultaRepository.findDailyMedic(m,data);}

    public Consulta getConsulta(Long id) { return consultaRepository.findById(id).get(); }

    public void updateConsulta(Consulta consulta) {
        consultaRepository.save(consulta);
    }
}
