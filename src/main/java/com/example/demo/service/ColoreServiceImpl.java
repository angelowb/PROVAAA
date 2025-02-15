package com.example.demo.service;

import com.example.demo.dao.ColoreDao;
import com.example.demo.model.Colore;
import com.example.demo.model.Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColoreServiceImpl implements ColoreService{
    @Autowired
    private ColoreDao coloreDao;
    @Override
    public Colore getColoreById(int id) {
        return coloreDao.findById(id).get();
    }

    @Override
    public List<Colore> getColori() {
        List<Colore> colori = (List<Colore>) coloreDao.findAll();
        return colori;
    }
}
