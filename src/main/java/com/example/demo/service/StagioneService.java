package com.example.demo.service;

import com.example.demo.model.Stagione;

import java.util.List;

public interface StagioneService {
    Stagione getStagioneById(int id);
    List<Stagione> getStagioni();
}
