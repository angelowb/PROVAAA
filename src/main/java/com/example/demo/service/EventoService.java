package com.example.demo.service;

import com.example.demo.model.Evento;

import java.util.List;

public interface EventoService {
    Evento getEventoById(int id);
    List<Evento> getEventi();
}
