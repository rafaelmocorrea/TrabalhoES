package com.esgrupo10.SATM.DTO;

import com.esgrupo10.SATM.model.InfoPagamento;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ConsultaDTO {

    private Long pedido_id;

    private String paciente_email;

    private String medico_email;

    private String descricao;

    private Float valor;

    private java.sql.Date data;

    private String hora;

    private Long consulta_id;

    private List<InfoPagamento> infoPagamentos;

}
