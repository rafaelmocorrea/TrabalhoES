package com.esgrupo10.SATM.DTO;

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

}
