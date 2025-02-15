package com.example.demo.service;

import com.example.demo.model.Colore;

import java.util.List;

public interface ColoreService {
    Colore getColoreById(int id);

    List<Colore> getColori();
}
