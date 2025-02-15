package com.example.demo.service;

import com.example.demo.model.Genere;

import java.util.List;

public interface GenereService {
    Genere getGenereById(int id);
    List<Genere> getGeneri();
}
