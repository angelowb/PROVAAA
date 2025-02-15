package com.example.demo.controller;

import com.example.demo.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String getPage(@RequestParam(name = "errore", required = false)String errore,
                          HttpSession session, Model model){
        if(session.getAttribute("cliente")!=null)
            return "redirect:/";
        model.addAttribute("errore", errore);
        return "login";
    }

    @PostMapping
    public String formManager(@RequestParam("username")String username,
                              @RequestParam("password")String password,
                              HttpSession session, Model model){
        if(!clienteService.controlloLogin(username, password, session))
            return "redirect:login?errore";

        return "redirect:/";
    }
}
