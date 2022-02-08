package com.esgrupo10.SATM.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "consulta")
@Data
@NoArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private java.sql.Date data;

    @Column(nullable = true)
    private Float valor;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = true)
    private String linkvideoconf;

    @Column(nullable = true)
    private String status;

    @Column(nullable = false)
    private java.sql.Time hora;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column
    private Boolean paga;

}
