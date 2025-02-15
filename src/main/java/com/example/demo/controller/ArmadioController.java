package com.example.demo.controller;

import com.example.demo.model.Capo;
import com.example.demo.model.Categoria;
import com.example.demo.model.Cliente;
import com.example.demo.service.CapoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/armadio")
public class ArmadioController {
    @Autowired
    private CapoService capoService;

    @GetMapping
    public String getPage(HttpSession session, Model model, @RequestParam(required = false) Integer categoriaId){
        if (session.getAttribute("cliente")==null)
            return "redirect:/login";
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        int clienteId = cliente.getIdCliente();
        if (categoriaId == null){
            List<Capo> capi = (List<Capo>) session.getAttribute("armadio");

            if (capi != null) {
                // Ordinare la lista in base all'ID decrescente (ultimo caricato per primo)
                capi.sort(Comparator.comparing(Capo::getIdCapo).reversed());
            }
            model.addAttribute("capi", session.getAttribute("armadio"));
        } else {
            List<Capo> capiUtenteFiltrati = capoService.getCapoByClienteFiltrati(clienteId, categoriaId);
            model.addAttribute("capi", capiUtenteFiltrati);
        }
        return "armadio";
    }
    @GetMapping("/elimina")
    public String eliminaCapo(HttpSession session, @RequestParam("id") int id){
        if (session.getAttribute("cliente")==null)
            return "redirect:/login";
        capoService.cancellaCapo(id, session);
        return "redirect:/";

    }
}
