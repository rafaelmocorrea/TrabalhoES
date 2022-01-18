package com.esgrupo10.SATM.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "pedidoconsulta")
@Data
@NoArgsConstructor
public class PedidoConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String especialidade;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = true)
    private Boolean atendida;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

}
