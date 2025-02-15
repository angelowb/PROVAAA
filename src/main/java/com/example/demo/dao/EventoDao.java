package com.example.demo.dao;

import com.example.demo.model.Evento;
import org.springframework.data.repository.CrudRepository;

public interface EventoDao extends CrudRepository<Evento, Integer> {
}
