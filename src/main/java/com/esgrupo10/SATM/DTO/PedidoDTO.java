package com.esgrupo10.SATM.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PedidoDTO {

    private Long pedido_id;

    private Long paciente_id;

    private String paciente_nome;

    private String paciente_cpf;

    private String descricao;
}
