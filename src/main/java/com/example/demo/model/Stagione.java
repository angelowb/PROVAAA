package com.example.demo.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stagione")
public class Stagione {
    @Id
    @Column(name = "id_stagione")
    private int idStagione;
    @Column
    private String stagione;

    @OneToMany(mappedBy = "stagione", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    List<Capo> capi = new ArrayList<>();

    public int getIdStagione() {
        return idStagione;
    }

    public void setIdStagione(int idStagione) {
        this.idStagione = idStagione;
    }

    public String getStagione() {
        return stagione;
    }

    public void setStagione(String stagione) {
        this.stagione = stagione;
    }
}
