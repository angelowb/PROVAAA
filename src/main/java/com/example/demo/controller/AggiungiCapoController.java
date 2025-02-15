package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/aggiungicapo")
public class AggiungiCapoController {
    @Autowired
    private CapoService capoService;
    @Autowired
    private GenereService genereService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private StagioneService stagioneService;
    @Autowired
    private EventoService eventoService;
    @Autowired
    private ColoreService coloreService;
    private Capo capo;
    private Map<String, String> errori;
   @GetMapping
   public String getPage(HttpSession session, Model model, @RequestParam(name = "id", required = false)Integer id) {
       if (session.getAttribute("cliente") == null) {
           return "redirect:/";
       }
       Cliente cliente = (Cliente) session.getAttribute("cliente");
       // Verifica se il cliente è autorizzato a modificare il capo
       if (id != null){
           int idDaVerificare = capoService.getIdClienteByCapo(id);
           if (idDaVerificare != cliente.getIdCliente()) {
               return "redirect:/";
           }
       }
       // Ottengo i dati per il form
       List<Capo> capi = capoService.getCapoByCliente(cliente.getIdCliente());
       List<Genere> generi = genereService.getGeneri();
       List<Categoria> categorie = categoriaService.getCategorie();
       List<Stagione> stagioni = stagioneService.getStagioni();
       List<Evento> eventi = eventoService.getEventi();
       List<Colore> colori = coloreService.getColori();
       if (errori == null){
           capo = id == null ? new Capo() : capoService.getCapoById(id);
       }

           model.addAttribute("capi", capi);
           model.addAttribute("generi", generi);
           model.addAttribute("categorie", categorie);
           model.addAttribute("stagioni", stagioni);
           model.addAttribute("eventi", eventi);
           model.addAttribute("colori", colori);
           model.addAttribute("capo", capo);
           model.addAttribute("errori", errori);
           return "aggiungi-capo";
   }



   @PostMapping
   public String formManager(@RequestParam("nome")String nome,
                              @RequestParam(name = "foto", required = false)MultipartFile foto,
                              @RequestParam("genere") int idGenere,
                              @RequestParam("categoria") int idCategoria,
                              @RequestParam("stagione") int idStagione,
                              @RequestParam("evento") int idEvento,
                              @RequestParam("colore") int idColore,
                              HttpSession session){

       Object risultatoValidazione = capoService.validaCapo(capo, nome, idGenere, idCategoria, idStagione, idEvento, idColore);
        if (risultatoValidazione != null){
            capo = (Capo) ((Object[])risultatoValidazione)[0];
            errori = (Map<String, String>) ((Object[])risultatoValidazione)[1];
            return "redirect:/aggiungicapo";
        }

        Cliente cliente = (Cliente) session.getAttribute("cliente");
        int idCliente = cliente.getIdCliente();

        capoService.aggiungiCapo(capo, nome, foto, idGenere, idCategoria, idStagione, idEvento, idCliente, idColore);
       // Aggiorna la lista dei capi e memorizzala in sessione
       List<Capo> capiAggiornati = capoService.getCapoByCliente(idCliente);
       session.setAttribute("armadio", capiAggiornati);
        capo = null;
        errori = null;
        return "redirect:/armadio";
   }
}
