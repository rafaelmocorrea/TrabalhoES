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
    private ConsultaRepository consultaRepository;

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

    public List<Consulta> listaConsultasPaciente(Paciente p) {
        return consultaRepository.findAllWithPatient(p);
    }

    public List<Consulta> listaConsultasMedico(Medico m)  {
        return consultaRepository.findAllWithMedic(m);
    }

    public List<Consulta> listaConsultasDiaPaciente(Paciente p,java.sql.Date data) { return consultaRepository.findDailyPatient(p,data);}

    public List<Consulta> listaConsultasDiaMedico(Medico m,java.sql.Date data) { return consultaRepository.findDailyMedic(m,data);}

    public List<Consulta> listaConsultasPreDateNotStatusMedico(Medico m, java.sql.Date data, String status) { return consultaRepository.findNotStatusPreDateMedic(m,status,data);}

    public List<Consulta> listaConsultasNotStatusMedico(Medico m, String status) {
        return consultaRepository.findNotStatusMedic(m,status);
    }

    public List<Consulta> listaConsultaStatusMedico(Medico m, String status) {
        return consultaRepository.findStatusMedic(m,status);
    }

    public Consulta getConsulta(Long id) { return consultaRepository.findById(id).get(); }

    public void updateConsulta(Consulta consulta) {
        consultaRepository.save(consulta);
    }

    public List<Consulta> listaConsultasNotStatusPaciente(Paciente pac, String status) {
        return consultaRepository.findNotStatusPatient(pac,status);
    }

    public List<Consulta> listaConsultaStatusPaciente(Paciente pac, String status) {
        return consultaRepository.findStatusPatient(pac,status);
    }

    public List<Consulta> listaConsultasNotStatusPacienteMedico(Paciente p, Medico m, String s) {
        return consultaRepository.findNotStatusPatientMedic(p,m,s);
    }

}
