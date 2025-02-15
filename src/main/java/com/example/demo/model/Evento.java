package com.example.demo.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evento")
public class Evento {
    @Id
    @Column(name = "id_evento")
    private int idEvento;
    @Column
    private String evento;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    List<Capo> capi = new ArrayList<>();


    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }
}
