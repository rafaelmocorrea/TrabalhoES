package com.esgrupo10.SATM.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "medico")
@Data
@NoArgsConstructor
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String senha;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = false)
    private String enderecopessoal;

    @Column(nullable = true, unique = false)
    private String telefone;

    @Column(nullable = true, unique = false)
    private String enderecoconsultorio;

    @Column(nullable = false, unique = true)
    private String crm;

    @Column(nullable = false, unique = false)
    private String especialidade;

}
