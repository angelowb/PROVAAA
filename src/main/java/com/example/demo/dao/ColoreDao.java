package com.example.demo.dao;

import com.example.demo.model.Capo;
import com.example.demo.model.Colore;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ColoreDao extends CrudRepository<Colore, Integer> {

}
