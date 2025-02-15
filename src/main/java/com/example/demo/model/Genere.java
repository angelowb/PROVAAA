package com.example.demo.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genere")
public class Genere {
    @Id
    @Column(name = "id_genere")
    private int idGenere;
    @Column
    private String genere;

    @OneToMany(mappedBy = "genere", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private List<Capo> capi = new ArrayList<>();


    public int getIdGenere() {
        return idGenere;
    }

    public void setIdGenere(int idGenere) {
        this.idGenere = idGenere;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }
}
