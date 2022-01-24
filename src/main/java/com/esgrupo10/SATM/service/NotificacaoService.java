package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.model.Paciente;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    public void notificaLink(Paciente pac, String link) {
//        Deve notificar o paciente por e-mail
        System.out.println("Enviando e-mail para "+pac.getEmail()+" com o link "+link+" ");
    }

    public void notificaConsulta(Paciente pac, Medico med, java.sql.Date data) {
        System.out.printf("Notificando o paciente "+pac.getNome()+" que o medico "+med.getNome()+" marcou consulta para "+ data);
    }

}
