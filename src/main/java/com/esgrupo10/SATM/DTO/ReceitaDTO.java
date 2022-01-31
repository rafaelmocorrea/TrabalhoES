package com.esgrupo10.SATM.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReceitaDTO {

    private String paciente_email;

    private String medico_email;

    private java.sql.Date data;

    private String remedios;

}
