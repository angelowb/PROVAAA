package com.example.demo.dao;

import com.example.demo.model.Capo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface CapoDao extends CrudRepository<Capo, Integer> {
    @Query(value = "SELECT * FROM capo WHERE fk_id_cliente = :idCliente", nativeQuery = true)
    List<Capo> findByFkIdCliente(int idCliente);

    @Query(value = "SELECT * FROM capo WHERE fk_id_cliente = :idCliente AND fk_id_categoria = :idCategoria", nativeQuery = true)
    List<Capo> findByFkIdClienteFkIdCategoria(int idCliente, int idCategoria);

    @Query(value = "SELECT fk_id_cliente FROM capo WHERE id_capo = :idCapo", nativeQuery = true)
    Integer findClienteIdByCapoId(Integer idCapo);
}
