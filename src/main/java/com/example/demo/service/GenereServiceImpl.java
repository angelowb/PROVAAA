package com.example.demo.service;

import com.example.demo.dao.GenereDao;
import com.example.demo.model.Genere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenereServiceImpl implements GenereService{
    @Autowired
    private GenereDao genereDao;
    @Override
    public Genere getGenereById(int id) {
        return genereDao.findById(id).get();
    }

    @Override
    public List<Genere> getGeneri() {
        List<Genere> generi = (List<Genere>) genereDao.findAll();
        return generi;
    }
}
