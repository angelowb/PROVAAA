package com.example.demo.controller;

import com.example.demo.model.Cliente;
import com.example.demo.service.CapoService;
import com.example.demo.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profilo")
public class ProfiloController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String getPage(HttpSession session, Model model){
        if (session.getAttribute("cliente")==null)
            return "redirect:/login";
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        model.addAttribute("cliente", cliente);
        return "profilo";
    }

    //CONTROLLO MODIFICA DATI PROFILO UTENTE
    @PostMapping
    public String formManager(@Valid @ModelAttribute("cliente")Cliente cliente, //validiamo, richiamiamo l'attribbuto cliente e lo associamo all'oggetto Cliente cliente
                              BindingResult result,
                              HttpSession session){
        if (result.hasErrors())
            return "profilo";
        clienteService.registrazioneCliente(cliente);
        session.setAttribute("cliente", cliente);
        return "redirect:/profilo";
    }
}
