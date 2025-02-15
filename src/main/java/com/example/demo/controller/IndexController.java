package com.example.demo.controller;

import com.example.demo.model.Capo;
import com.example.demo.model.Cliente;
import com.example.demo.service.CapoService;
import com.example.demo.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    private CapoService capoService;
    @GetMapping
    public String getPage(HttpSession session,
                          Model model, @RequestParam(required = false)String elegante,
                          @RequestParam(required = false)String sportivo,
                          @RequestParam(required = false)String casual){
        if (session.getAttribute("cliente")==null)
            return "redirect:/login";
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        model.addAttribute("cliente", cliente);
        int clienteId = cliente.getIdCliente();
        //MI TROVO LA LISTA DELL'UTENTE PER ESPORTARE IL NUMERO DI CAPI CHE HA
        List<Capo> capiUtente = capoService.getCapoByCliente(clienteId);
        int numeroRecord = capiUtente.size();
        session.setAttribute("armadio", capiUtente);
        model.addAttribute("numeroCapi", numeroRecord);
        //CONTROLLO SULL'EVENTO SELEZIONATO E RECUPERO L'ID DAL DB
        int eventoSelezionato = 0;
        if (elegante != null){
            eventoSelezionato = 1;
        } else if (sportivo != null){
            eventoSelezionato = 2;
        } else if(casual != null){
            eventoSelezionato = 3;
        }
        model.addAttribute("eventoSelezionato", eventoSelezionato);
        //recupero il mese corrente
        LocalDate currentDate = LocalDate.now();
        int meseCorrente = currentDate.getMonthValue();
        int mesePeriodoLungo = 0;
        switch (meseCorrente) {
            case 3:
            case 4:
            case 5:
                meseCorrente = 2;
                mesePeriodoLungo = 6;
                break;
            case 6:
            case 7:
            case 8:
                meseCorrente = 1;
                mesePeriodoLungo = 6;
                break;
            case 9:
            case 10:
            case 11:
                meseCorrente = 4;
                mesePeriodoLungo = 7;
                break;
            default:
                meseCorrente = 3;
                mesePeriodoLungo = 7;
                break;
        }
        List<Capo> risultato = capoService.findRandomByClienteEventoStagioneCategoria(eventoSelezionato, meseCorrente, mesePeriodoLungo, session);

        if (risultato.isEmpty()){
            model.addAttribute("testoErrori", "Non hai abbastanza capi per fare CLOYOU, aggiungi tutti i capi del tuo armadio per un risultato migliore");
        } else {
            model.addAttribute("testoErrori", "Ecco il tuo CLOYOU per l'evento");
        }

        model.addAttribute("risultato", risultato);
        return "index";
    }

    @GetMapping("/logout")
    public String clienteLogout(HttpSession session){
        session.removeAttribute("cliente");
        return "redirect:/login";
    }

}
