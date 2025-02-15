package com.example.demo.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @Column(name = "id_categoria")
    private int idCategoria;
    @Column
    private String categoria;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    List<Capo> capi = new ArrayList<>();


    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
