package com.esgrupo10.SATM.DTO;

import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.model.Paciente;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExameDTO {

    private String nome;

    private java.sql.Date data;

    private String paciente_email;

    private String medico_email;

    private String descricao;

    private Paciente paciente;

    private Medico medico;

    private Boolean feito;

}
