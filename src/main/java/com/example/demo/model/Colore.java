package com.example.demo.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "colore")
public class Colore {
    @Id
    @Column(name = "id_colore")
    private int idColore;
    @Column
    private String colore;



    public int getIdColore() {
        return idColore;
    }

    public void setIdColore(int idColore) {
        this.idColore = idColore;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }
}
