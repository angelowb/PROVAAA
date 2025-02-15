package com.example.demo.service;

import com.example.demo.dao.StagioneDao;
import com.example.demo.model.Stagione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StagioneServiceImpl implements StagioneService{
    @Autowired
    private StagioneDao stagioneDao;

    @Override
    public Stagione getStagioneById(int id) {
        return stagioneDao.findById(id).get();
    }

    @Override
    public List<Stagione> getStagioni() {
        List<Stagione> stagioni = (List<Stagione>) stagioneDao.findAll();
        return stagioni;
    }
}
