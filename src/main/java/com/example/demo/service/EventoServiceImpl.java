package com.example.demo.service;

import com.example.demo.dao.EventoDao;
import com.example.demo.model.Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoServiceImpl implements EventoService{
    @Autowired
    private EventoDao eventoDao;
    @Override
    public Evento getEventoById(int id) {
        return eventoDao.findById(id).get();
    }

    @Override
    public List<Evento> getEventi() {
        List<Evento> eventi = (List<Evento>) eventoDao.findAll();
        return eventi;
    }
}
