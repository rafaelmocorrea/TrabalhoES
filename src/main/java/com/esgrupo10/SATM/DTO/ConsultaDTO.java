package com.esgrupo10.SATM.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConsultaDTO {

    private Long pedido_id;

    private String paciente_email;

    private String medico_email;

    private String descricao;

    private Float valor;

    private java.sql.Date data;

    private Long consulta_id;

}
