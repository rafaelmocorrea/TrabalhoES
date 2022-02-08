package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.model.Paciente;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class NotificacaoService {

    public void notificaLink(Paciente pac, String link) {
//        Deve notificar o paciente por e-mail
        System.out.println("Enviando e-mail para "+pac.getEmail()+" com o link "+link+" ");
    }

    public void notificaConsulta(Paciente pac, Medico med, java.sql.Date data) {
        System.out.printf("Notificando o paciente "+pac.getNome()+" que o medico "+med.getNome()+" marcou consulta para "+ data);
    }

    public void notificaLinkUpdate(Paciente pac, String link) {
        System.out.println("Email para "+pac.getEmail()+" link atualizado: "+link);
    }

    public void notificaConsultaCancelada(Paciente paciente, Medico medico, java.sql.Date data) {
        System.out.println("Email para: "+ paciente.getEmail()+"\nO medico "+medico.getNome()+" ("+medico.getEmail()+") cancelou sua consulta do dia "+data+".");
    }

    public void notificaConsultaAceita(Paciente pac, Medico med, java.sql.Date data) {
        System.out.println("Email para: "+pac.getEmail()+"\nO medico "+med.getNome()+" ("+med.getEmail()+") confirmou sua consulta para o dia "+data+".");
    }

    public void notificaReceita(Paciente pac, Medico med) {
        System.out.println("Email para: "+pac.getEmail()+"\nO medico "+med.getNome()+" ("+med.getEmail()+") prescreveu uma receita.");
    }

    public void notificaExame(Paciente pac, Medico med, String nome) {
        System.out.println("Email para: "+pac.getEmail()+"\nO medico "+med.getNome()+" ("+med.getEmail()+") solicitou um exame : "+nome+".");
    }

    public void notificaExameApagado(Paciente pac, Medico med, String nome) {
        System.out.println("Email para: "+pac.getEmail()+"\nO medico "+med.getNome()+" ("+med.getEmail()+") cancelou um exame : "+nome+".");
    }

    public void notificaExameUpload(Paciente pac, Medico med, String nome) {
        System.out.println("Email para: "+med.getEmail()+"\nO paciente "+pac.getNome()+" ("+pac.getEmail()+") fez o upload do exame "+nome+".");
    }

}
