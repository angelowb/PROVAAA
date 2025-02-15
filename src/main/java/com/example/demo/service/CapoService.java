package com.example.demo.service;

import com.example.demo.model.Capo;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CapoService {
    List<Capo> getCapoByCliente(int idCliente);

    List<Capo> getCapoByClienteFiltrati(int idCliente, int idCategoria);

    int getIdClienteByCapo(int idCapo);

    Object validaCapo(Capo capo, String nome, int idGenere, int idCategoria, int idStagione, int idEvento, int idColore);

    void aggiungiCapo(Capo capo, String nome, MultipartFile foto, int idGenere, int idCategoria, int idStagione, int idEvento, int idCliente, int idColore);

    Capo getCapoById(int id);

    List<Capo>  findRandomByClienteEventoStagioneCategoria(int eventoSelezionato, int meseCorrente, int mesePeriodoLungo, HttpSession session);

    void cancellaCapo (int id, HttpSession session);
}
