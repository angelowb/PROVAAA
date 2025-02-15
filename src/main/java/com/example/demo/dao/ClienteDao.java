package com.example.demo.dao;

import com.example.demo.model.Capo;
import com.example.demo.model.Cliente;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClienteDao  extends CrudRepository<Cliente, Integer> {
    Cliente findByUsernameAndPassword(String username, String password);

    Cliente findByUsername(String username);

}
