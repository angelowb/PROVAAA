package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private int idCliente;
    @Column
    @Pattern(regexp = "[a-zA-Zàèìù\\s']{3,50}", message = "Nome non valido")
    private String nome;
    @Column
    @Pattern(regexp = "[a-zA-Zàèìù\\s']{3,50}", message = "Cognome non valido")
    private String cognome;
    @Column(name = "data_di_nascita")
    private Date dataDiNascita;
    @Column
    private String sesso;
    @Column
    @Pattern(regexp = "[a-zA-Z0-9._-]{1,50}", message = "Caratteri non ammessi")
    private String username;
    @Column
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,50}", message = "Password inadeguata")
    private String password;

    @Column
    private String email;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    List<Capo> capi = new ArrayList<>();


    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public void setDataDiNascita(Date dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}
}
