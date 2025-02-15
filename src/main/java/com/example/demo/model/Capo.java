package com.example.demo.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "capo")
public class Capo {
    @Id
    @Column(name = "id_capo")
    private int idCapo;
    @Column
    private String nome;
    @Column
    private String foto;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "fk_id_genere", referencedColumnName = "id_genere")
    private Genere genere;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "fk_id_categoria", referencedColumnName = "id_categoria")
    private Categoria categoria;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "fk_id_stagione", referencedColumnName = "id_stagione")
    private Stagione stagione;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "fk_id_cliente", referencedColumnName = "id_cliente")
    private Cliente cliente;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "fk_id_evento", referencedColumnName = "id_evento")
    private Evento evento;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "fk_id_colore", referencedColumnName = "id_colore")
    private Colore colore;

    //TODO da verificare come mai questi non sono obbligatori





    public int getIdCapo() {
        return idCapo;
    }

    public void setIdCapo(int idCapo) {
        this.idCapo = idCapo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Genere getGenere() {
        return genere;
    }

    public void setGenere(Genere genere) {
        this.genere = genere;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Stagione getStagione() {
        return stagione;
    }

    public void setStagione(Stagione stagione) {
        this.stagione = stagione;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Evento getEvento() {
        return evento;
    }


    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Colore getColore() {
        return colore;
    }

    public void setColore(Colore colore) {
        this.colore = colore;
    }
}
