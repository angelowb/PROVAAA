package com.example.demo.service;

import com.example.demo.dao.CapoDao;
import com.example.demo.model.*;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CapoServiceImpl implements CapoService{
    @Autowired
    private CapoDao capoDao;
    @Autowired
    private ClienteService clienteService;
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

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads";

    //RITORNA L'ARMADIO DELL'UTENTE COLLEGATO
    @Override
    public List<Capo> getCapoByCliente(int idCliente) {
        return capoDao.findByFkIdCliente(idCliente);
    }
    //MI RITORAN I CAPI FILTRATI NELL'ARMADIO
    @Override
    public List<Capo> getCapoByClienteFiltrati(int idCliente, int idCategoria){
        return capoDao.findByFkIdClienteFkIdCategoria(idCliente, idCategoria);
    }

    //MI RITORNA L'ID DEL CLIENTE DEL CAPO MODIFICATO
    @Override
    public int getIdClienteByCapo(int idCapo){
        return capoDao.findClienteIdByCapoId(idCapo);
    }

    @Override
    public Object validaCapo(Capo capo, String nome, int idGenere, int idCategoria, int idStagione, int idEvento, int idColore) {
        capo.setGenere(genereService.getGenereById(idGenere));
        capo.setCategoria(categoriaService.getCategoriaById(idCategoria));
        capo.setStagione(stagioneService.getStagioneById(idStagione));
        capo.setEvento(eventoService.getEventoById(idEvento));
        capo.setColore(coloreService.getColoreById(idColore));
        capo.setNome(nome);
        Map<String, String> errori = new HashMap<>();
        if (!Pattern.matches("[a-zA-z0-9\\sàèòùì,.-]{1,32}",nome))
            errori.put("nome","Caratteri non ammessi");
        if (errori.size() > 0)
            return new Object[]{capo, errori};
        else
            return null;
    }

    @Override
    public void aggiungiCapo(Capo capo, String nome, MultipartFile foto, int idGenere, int idCategoria, int idStagione, int idEvento, int idCliente, int idColore) {
        capo.setNome(nome);

        if (foto != null && !foto.isEmpty()) {
            try (InputStream inputStream = foto.getInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[8192]; // Legge l'immagine in blocchi da 8KB
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                String formato = foto.getContentType();
                String fotoCodificata = "data:" + formato + ";base64," +
                        Base64.getEncoder().encodeToString(outputStream.toByteArray());

                capo.setFoto(fotoCodificata);

            } catch (IOException e) {
                System.out.println("Errore durante la codifica dell'immagine: " + e.getMessage());
            }
        }

        capo.setGenere(genereService.getGenereById(idGenere));
        capo.setCategoria(categoriaService.getCategoriaById(idCategoria));
        capo.setStagione(stagioneService.getStagioneById(idStagione));
        capo.setEvento(eventoService.getEventoById(idEvento));
        capo.setCliente(clienteService.getClientiById(idCliente));
        capo.setColore(coloreService.getColoreById(idColore));
        capoDao.save(capo);
    }
    
    @Override
    public Capo getCapoById(int id) {
        Optional<Capo> capoOptional = capoDao.findById(id);
        if (capoOptional.isPresent())
            return capoOptional.get();
        return null;
    }

    @Override
    public void cancellaCapo(int id, HttpSession session){
        //PRENDO L'ID CLIENTE DAL CAPO CHE SI VUOLE ELIMINARE
        int capoDaEliminare = getCapoById(id).getCliente().getIdCliente();
        //PRENDO L'ID CLIENTE DEL CLIENTE SALVATO IN SESSIONE
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        //CONTROLLO SE L'UTENTE STA ELIMINANDO UN SUO CAPO
        if (capoDaEliminare == cliente.getIdCliente()){
            capoDao.deleteById(id);
        }

    }
    //Algoritmo principale generazione outfit
    @Override
    public List<Capo> findRandomByClienteEventoStagioneCategoria(int eventoSelezionato, int meseCorrente, int mesePeriodoLungo, HttpSession session) {
        List<Capo> risultato = new ArrayList<>();
        //RIPRENDO L'ARMADIO DEL CLIENTE SPECIFICO NELLA SESSIONE
        List<Capo> capi = (List<Capo>) session.getAttribute("armadio");

        //PRIMO CONTROLLO SE HO ABITI LUNGHI IN BASE AD UNA PROBABILITA'
        Random random = new Random();
        int r1 = 18;
        int r2 = random.nextInt(18)+1;
        System.out.println("Numero casuale: " +r2);
        Capo primoCapo = null;
        if (r1 == r2){
            List<Capo> capiInteri = filterCapoByCategoriaIntero(capi, eventoSelezionato, meseCorrente, mesePeriodoLungo);
            if (!capiInteri.isEmpty()){
                primoCapo = capiInteri.get(random.nextInt(capiInteri.size()));
                risultato.add(primoCapo);
                //MI SELEZIONO IL COLORE DEL CAPO INTERO PER RICHIAMARE I PIEDI
                String colore = primoCapo.getColore().getColore();
                String[] coloriDaGestire = gestioneColori(colore);
                List<Capo> piediFiltrati = filterPiediByEventoStagione(capi, eventoSelezionato, meseCorrente, mesePeriodoLungo, coloriDaGestire);
                if (!piediFiltrati.isEmpty()){
                    Capo terzoCapo = piediFiltrati.get(random.nextInt(piediFiltrati.size()));
                    risultato.add(terzoCapo);
                }
                return risultato;
            }
        }


        //INIZIO A FILTRARE IL PRIMO CAPO DAL BUSTO
        List<Capo> capiFiltrati = filterCapiByEventoStagioneCategoria(capi, eventoSelezionato, meseCorrente, mesePeriodoLungo);
        //GENERATA LA LISTA DI CAPI FILTRATI MI FACCIO UN RANDOM PER ESTRARNE UNO
        if (capiFiltrati.isEmpty()){
            return new ArrayList<>();
        }
        primoCapo = capiFiltrati.get(random.nextInt(capiFiltrati.size()));
        risultato.add(primoCapo);

        //MI SELEZIONO IL COLORE DEL CAPO ESTRATTO PER SELEZIONARE LE GAMBE ABBINATE
        String colore = primoCapo.getColore().getColore();


        // SELEZIONO LA SECONDA PARTE DELL'OUTFIT
        String[] coloriDaGestire = gestioneColori(colore); //GLI PASSO LA FUNZIONE DEI COLORI
        List<Capo> gambeFiltrate = filterGambeByEventoStagioneColore(capi, eventoSelezionato, meseCorrente, mesePeriodoLungo, coloriDaGestire);
        Capo secondoCapo = null;
        if (!gambeFiltrate.isEmpty()){
            secondoCapo = gambeFiltrate.get(random.nextInt(gambeFiltrate.size())); //SE E VUOTO NE PRENDO UNO A CASO ANCHE SE IL COLORE NON CORRISPONDE
        }
        if (secondoCapo != null) {
            risultato.add(secondoCapo);
        }

        //CREA UNA LISTA DI CAPI PIEDI
        List<Capo> piediFiltrati = filterPiediByEventoStagione(capi, eventoSelezionato, meseCorrente, mesePeriodoLungo, coloriDaGestire);
        if (!piediFiltrati.isEmpty()){
            Capo terzoCapo = piediFiltrati.get(random.nextInt(piediFiltrati.size()));
            risultato.add(terzoCapo);
        }

        //CREO COMBO PER GENERARE OUTFIT
        System.out.println("EventoSelezionato: "+eventoSelezionato);

        if (eventoSelezionato == 1 || eventoSelezionato == 3){
            if (primoCapo.getGenere().getGenere().equals("t-shirt") || primoCapo.getGenere().getGenere().equals("camicia")){
                List<Capo> capiAggiuntivi = filterCapoByGenere(capi, meseCorrente, mesePeriodoLungo, coloriDaGestire);
                if (!capiAggiuntivi.isEmpty()){
                    Capo quartoCapo = capiAggiuntivi.get(random.nextInt(capiAggiuntivi.size()));
                    risultato.add(quartoCapo);
                }
            }
        }

        System.out.println("Risultato: "+risultato);
        //RITORNA IL RISULTATO
        return risultato;
    }


    //FILTRO PER ABITI LUNGHI
    private List<Capo> filterCapoByCategoriaIntero(List<Capo> capi, int eventoSelezionato, int meseCorrente, int mesePeriodoLungo){
        return capi.stream()
                .filter(c -> c.getEvento().getIdEvento() == eventoSelezionato)
                .filter(c -> c.getStagione().getIdStagione() == meseCorrente || c.getStagione().getIdStagione() == mesePeriodoLungo ||c.getStagione().getIdStagione() == 5)
                .filter(c -> c.getCategoria().getIdCategoria() == 5)
                .collect(Collectors.toList());
    }

    //FILTRA I CAPI IN BASE ALLA CATEGORIA BUSTO E LA STAGIONE SELEZIONATA + PERIODO LUNGO CHE GESTISCE ANCHE I DOPPI MESI
    private List<Capo> filterCapiByEventoStagioneCategoria(List<Capo> capi, int eventoSelezionato, int meseCorrente, int mesePeriodoLungo){
        return capi.stream()
                .filter(c -> c.getEvento().getIdEvento() == eventoSelezionato)
                .filter(c -> c.getStagione().getIdStagione() == mesePeriodoLungo || c.getStagione().getIdStagione() == 5)
                .filter(c -> c.getCategoria().getIdCategoria() == 1)
                .collect(Collectors.toList());
    }

    //FILTRA I CAPI GAMBE IN BASE ALLA STAGIONE ED EVENTO e COLORE SE LO TIENE
    private List<Capo> filterGambeByEventoStagioneColore(List<Capo> capi, int eventoSelezionato, int meseCorrente, int mesePeriodoLungo, String[] coloriDaGestire){
        List<Capo> capiGambe = capi.stream()
                .filter(c -> c.getEvento().getIdEvento() == eventoSelezionato)
                .filter(c -> c.getStagione().getIdStagione() == mesePeriodoLungo || c.getStagione().getIdStagione() == 5)
                .filter(c -> c.getCategoria().getIdCategoria() == 2)
                .collect(Collectors.toList());
        if (!capiGambe.isEmpty()){
            for (int i = 0; i<4; i++){
                int finalI = i;
                List<Capo> capiGambeFiltroColore = capiGambe.stream()
                        .filter(c -> c.getColore().getColore().equals(coloriDaGestire[finalI]))
                        .collect(Collectors.toList());
                if (!capiGambeFiltroColore.isEmpty()){
                    return capiGambeFiltroColore;
                }
            }
            return capiGambe;
        }
        return new ArrayList<>();
    }

    //ALGORITMO CHE GESTISCE I COLORI
    private String[] gestioneColori(String colore){
        String[] sceltaColori = new String[4];
        if (colore.equals("Nero") ){
            sceltaColori[0] =  "Bianco";
            sceltaColori[1] =  "Viola";
            sceltaColori[2] = "Celeste";
            sceltaColori[3] =  "Giallo";
        } else if (colore.equals("Celeste")){
            sceltaColori[0] =  "Bianco";
            sceltaColori[1] =  "Grigio";
            sceltaColori[2] = "Giallo";
            sceltaColori[3] ="Nero";
        } else if (colore.equals("Giallo")) {
            sceltaColori[0] = "Bianco";
            sceltaColori[1] = "Celeste";
            sceltaColori[2] = "Nero";
            sceltaColori[3] ="Verde";
        } else if (colore.equals("Marrone")){
            sceltaColori[0] = "Bianco";
            sceltaColori[1] = "Rosa";
            sceltaColori[2] = "Rosso";
            sceltaColori[3] ="Blu";
        } else if(colore.equals("Rosso")){
            sceltaColori[0] = "Bianco";
            sceltaColori[1] = "Celeste";
            sceltaColori[2] = "Verde";
            sceltaColori[3] ="Rosa";
        } else if (colore.equals("Blu")){
            sceltaColori[0] = "Bianco";
            sceltaColori[1] = "Nero";
            sceltaColori[2] = "Marrone";
            sceltaColori[3] ="Rosso";
        } else if (colore.equals("Viola")) {
            sceltaColori[0] = "Bianco";
            sceltaColori[1] = "Arancione";
            sceltaColori[2] = "Nero";
            sceltaColori[3] ="Rosa";
        } else if(colore.equals("Arancione")){
            sceltaColori[0] = "Bianco";
            sceltaColori[1] = "Nero";
            sceltaColori[2] = "Verde";
            sceltaColori[3] ="Viola";
        } else if (colore.equals("Verde")) {
            sceltaColori[0] = "Nero";
            sceltaColori[1] = "Grigio";
            sceltaColori[2] = "Giallo";
            sceltaColori[3] ="Rosso";
        } else if (colore.equals("Grigio")){
            sceltaColori[0] = "Bianco";
            sceltaColori[1] = "Celeste";
            sceltaColori[2] = "Verde";
            sceltaColori[3] ="Nero";
        } else if (colore.equals("Rosa")) {
            sceltaColori[0] = "Bianco";
            sceltaColori[1] = "Blu";
            sceltaColori[2] = "Nero";
            sceltaColori[3] ="Rosso";
        } else if (colore.equals("Bianco")) {
            sceltaColori[0] = "Bianco";
            sceltaColori[1] = "Celeste";
            sceltaColori[2] = "Verde";
            sceltaColori[3] ="Blu";
        } else if (colore.equals("Beige")) {
            sceltaColori[0] = "Rosso";
            sceltaColori[1] = "Arancione";
            sceltaColori[2] = "Blu";
            sceltaColori[3] ="Verde";
        }
        mischiaArray(sceltaColori);
        return sceltaColori;
    }



    private void mischiaArray(String[] array) {
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Scambia l'elemento corrente con uno casuale selezionato
            String temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    //TROVA UNA LISTA DI CAPI PIEDI IN BASE ALLA STAGIONE E EVENTO
    private List<Capo> filterPiediByEventoStagione(List<Capo> capi, int eventoSelezionato, int meseCorrente, int mesePeriodoLungo, String[] coloriDaGestire){
        List<Capo> capiPiedi = capi.stream()
                .filter(c -> c.getEvento().getIdEvento() == eventoSelezionato)
                .filter(c -> c.getStagione().getIdStagione() == mesePeriodoLungo ||c.getStagione().getIdStagione() == 5)
                .filter(c -> c.getCategoria().getIdCategoria() == 3)
                .collect(Collectors.toList());
        if (!capiPiedi.isEmpty()){
            for (int i = 0; i<4; i++){
                int finalI = i;
                List<Capo> capiPiediColore = capiPiedi.stream()
                        .filter(c -> c.getColore().getColore().equals(coloriDaGestire[finalI]))
                        .collect(Collectors.toList());
                if (!capiPiediColore.isEmpty()){
                    return capiPiediColore;
                }
            }
        }
        return capiPiedi;
    }

    private List<Capo> filterCapoByGenere(List<Capo> capi, int meseCorrente, int mesePeriodoLungo, String[] coloriDaGestire){
         List<Capo> capiDaAggiungere = capi.stream()
                .filter(c -> c.getGenere().getGenere().equals("giacca")  || c.getGenere().getGenere().equals("maglione"))
                .filter(c -> c.getStagione().getIdStagione() == mesePeriodoLungo ||c.getStagione().getIdStagione() == 5)
                .collect(Collectors.toList());
         if (!capiDaAggiungere.isEmpty()){
             for (int i = 0; i<4; i++){
                 int finalI = i;
                 List<Capo> capiDaAggiungereColore = capiDaAggiungere.stream()
                         .filter(c -> c.getColore().getColore().equals(coloriDaGestire[finalI]))
                         .collect(Collectors.toList());
                 if (!capiDaAggiungereColore.isEmpty()){
                     return capiDaAggiungereColore;
                 }
             }
         }
        return capiDaAggiungere;

    }


}
