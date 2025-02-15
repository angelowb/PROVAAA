package com.example.demo.service;

import com.example.demo.dao.ClienteDao;
import com.example.demo.model.Cliente;
import com.example.demo.model.Evento;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService{
    @Autowired
    private ClienteDao clienteDao;
    @Override
    public boolean controlloLogin(String username, String password, HttpSession session) {
        Cliente cliente = clienteDao.findByUsernameAndPassword(username, password);
        if(cliente != null){
            session.setAttribute("cliente", cliente);
            return true;
        }
        return false;
    }

    @Override
    public boolean controlloUsername(String username) {
        if (clienteDao.findByUsername(username)==null)
            return true;
        return false;
    }
    @Override
    public void registrazioneCliente(Cliente cliente){
        clienteDao.save(cliente);
    }

    @Override
    public Cliente getClientiById(int id) {
        return clienteDao.findById(id).get();
    }
}
