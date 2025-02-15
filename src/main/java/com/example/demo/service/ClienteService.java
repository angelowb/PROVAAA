package com.example.demo.service;

import com.example.demo.model.Cliente;
import com.example.demo.model.Evento;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface ClienteService {
    boolean controlloLogin(String username, String password, HttpSession session);

    boolean controlloUsername(String username);
    void registrazioneCliente(Cliente cliente);

    Cliente getClientiById(int id);
}
