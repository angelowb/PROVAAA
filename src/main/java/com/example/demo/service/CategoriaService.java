package com.example.demo.service;

import com.example.demo.model.Categoria;

import java.util.List;

public interface CategoriaService {
    Categoria getCategoriaById(int id);
    List<Categoria> getCategorie();
}
