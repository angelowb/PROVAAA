package com.example.demo.service;

import com.example.demo.dao.CategoriaDao;
import com.example.demo.model.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService{
    @Autowired
    private CategoriaDao categoriaDao;
    @Override
    public Categoria getCategoriaById(int id) {
        return categoriaDao.findById(id).get();
    }

    @Override
    public List<Categoria> getCategorie() {
        List<Categoria> categorie = (List<Categoria>) categoriaDao.findAll();
        return categorie;
    }
}
