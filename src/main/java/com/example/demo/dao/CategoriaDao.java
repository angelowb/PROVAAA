package com.example.demo.dao;

import com.example.demo.model.Categoria;
import org.springframework.data.repository.CrudRepository;

public interface CategoriaDao extends CrudRepository<Categoria, Integer> {
}
