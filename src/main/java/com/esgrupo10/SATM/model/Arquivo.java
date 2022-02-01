package com.esgrupo10.SATM.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name="arquivo")
@Data
@NoArgsConstructor
public class Arquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String docName;

    @Column(nullable = false)
    private String docType;

    @Lob
    private byte[] dados;

}
