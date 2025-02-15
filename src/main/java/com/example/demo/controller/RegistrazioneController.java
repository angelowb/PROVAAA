package com.example.demo.controller;

import com.example.demo.model.Cliente;
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
@RequestMapping("/registrazione")
public class RegistrazioneController {
    @Autowired
    private ClienteService clienteService;
    @GetMapping
    public String getPage(HttpSession session, Model model){
        if (session.getAttribute("cliente")!=null)
            return "redirect:/";
        Cliente cliente = new Cliente();
        model.addAttribute("cliente", cliente);
        return "registrazione";
    }

    @PostMapping
    public String formManager(@Valid @ModelAttribute("cliente")Cliente cliente, BindingResult result, Model model){
        if (result.hasErrors())
            return "registrazione";
        if (!clienteService.controlloUsername(cliente.getUsername())){
            model.addAttribute("duplicato", "Username non disponibile");
            return "registrazione";
        }
        clienteService.registrazioneCliente(cliente);
        return "redirect:/login";
    }
}
