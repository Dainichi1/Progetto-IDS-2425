package unicam.filiera.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unicam.filiera.models.Prodotto;
import unicam.filiera.request.ProdottoRimandatoRequest;
import unicam.filiera.services.ProdottoService;

import java.util.List;

@RestController
@RequestMapping("/api/prodotti")
public class ProdottoController {

    @Autowired
    private ProdottoService prodottoService;

    /**
     * Crea un nuovo prodotto e lo invia al curatore per l'approvazione.
     */
    @PostMapping("/{staff}/create")
    public ResponseEntity<String> createProdottoForStaff(
            @PathVariable String staff,
            @RequestParam String name,
            @RequestParam Double price,
            @RequestParam String category,
            @RequestParam String info,
            @RequestParam Integer availability,
            @RequestParam MultipartFile images,
            @RequestParam MultipartFile certificato) {

        try {
            prodottoService.createProdotto(name, price, category, info, availability, images, certificato, staff.toUpperCase());
            return ResponseEntity.ok("Prodotto creato con successo per il ruolo " + staff);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante la creazione del prodotto: " + e.getMessage());
        }
    }



    /**
     * Restituisce la lista dei prodotti in attesa di approvazione per il curatore.
     */
    @GetMapping("/curatore/list")
    public ResponseEntity<List<Prodotto>> getProdottiPerCuratore() {
        try {
            List<Prodotto> prodotti = prodottoService.getProdottiInAttesa();
            return ResponseEntity.ok(prodotti);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Gestisce l'approvazione o il rifiuto di un prodotto da parte del curatore.
     */
    @PostMapping("/curatore/action")
    public ResponseEntity<String> azioneCuratore(
            @RequestParam Long prodottoId,
            @RequestParam String action,
            @RequestParam(required = false) String curatorComments) {
        try {
            prodottoService.gestisciProdotto(prodottoId, action, curatorComments);
            if ("approva".equalsIgnoreCase(action)) {
                return ResponseEntity.ok("Prodotto approvato con successo.");
            } else if ("rimanda".equalsIgnoreCase(action)) {
                return ResponseEntity.ok("Prodotto rimandato al produttore.");
            } else {
                return ResponseEntity.badRequest().body("Azione non valida: " + action);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore interno del server: " + e.getMessage());
        }
    }

    /**
     * Recupera tutti i prodotti con stato approvato.
     */
    @GetMapping("/approvati")
    public ResponseEntity<List<Prodotto>> getAllProdottiApprovati() {
        try {
            List<Prodotto> prodotti = prodottoService.getProdottiApprovati();
            return ResponseEntity.ok(prodotti);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    /**
     * Recupera i prodotti con stato rimandato per il ruolo specificato.
     */
    @GetMapping("/{staff}/rimandati")
    public ResponseEntity<List<Prodotto>> getProdottiRimandatiPerRuolo(@PathVariable String staff) {
        try {
            List<Prodotto> prodotti = prodottoService.getProdottiRimandatiPerStaff(staff.toUpperCase());
            return ResponseEntity.ok(prodotti);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Recupera tutti i prodotti con stato approvato per il ruolo specificato.
     */
    @GetMapping("/{staff}/approvati")
    public ResponseEntity<List<Prodotto>> getProdottiApprovatiPerRuolo(@PathVariable String staff) {
        try {
            List<Prodotto> prodotti = prodottoService.getProdottiApprovatiPerStaff(staff.toUpperCase());
            return ResponseEntity.ok(prodotti);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    /**
     * Consente al ruolo specificato di rinviare un prodotto al curatore.
     */
    @PostMapping("/{staff}/resend")
    public ResponseEntity<String> resendProdotto(
            @PathVariable String staff,
            @RequestBody ProdottoRimandatoRequest request) {
        try {
            Long prodottoId = request.getProdottoId();
            prodottoService.resendProdotto(prodottoId, staff.toUpperCase());
            return ResponseEntity.ok("Prodotto inviato nuovamente al curatore.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore durante l'invio del prodotto: " + e.getMessage());
        }
    }

    /**
     * Aggiorna i dettagli di un prodotto.
     */
    @PostMapping("{staff}/update")
    public ResponseEntity<String> updateProdotto(@RequestBody Prodotto updatedProdotto) {
        try {
            prodottoService.updateProdotto(updatedProdotto);
            return ResponseEntity.ok("Prodotto aggiornato con successo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore durante l'aggiornamento del prodotto: " + e.getMessage());
        }
    }
}