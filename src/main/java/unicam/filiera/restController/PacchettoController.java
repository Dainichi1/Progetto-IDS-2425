package unicam.filiera.restController;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unicam.filiera.dtos.PacchettoCreationDTO;
import unicam.filiera.models.Pacchetto;
import unicam.filiera.request.PacchettoRimandatoRequest;
import unicam.filiera.services.PacchettoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller per gestire le operazioni sui Pacchetti.
 */
@RestController
@RequestMapping("/api/pacchetti")
public class PacchettoController {

    private static final Logger logger = LoggerFactory.getLogger(PacchettoController.class);

    @Autowired
    private PacchettoService pacchettoService;

    /**
     * Crea un nuovo pacchetto (per uno staff, ad esempio DISTRIBUTORE)
     * e lo invia al Curatore per l'approvazione.
     */
    @PostMapping(value = "/{staff}/create", consumes = {"multipart/form-data"})
    public ResponseEntity<Object> createPacchettoForStaff(
            @PathVariable String staff,
            @Valid @ModelAttribute PacchettoCreationDTO dto,
            BindingResult bindingResult
    ) {
        Map<String, String> errors = new HashMap<>();

        // LOG dei file
        logger.info("Received DTO Pacchetto => name='{}', price='{}', category='{}', info='{}', availability='{}', imagesCount={}, certificatiCount={}",
                dto.getName(), dto.getPrice(), dto.getCategory(), dto.getInfo(), dto.getAvailability(),
                (dto.getImages() != null ? dto.getImages().size() : 0),
                (dto.getCertificati() != null ? dto.getCertificati().size() : 0)
        );

        if (dto.getImages() != null && dto.getCertificati() != null) {
            if (dto.getImages().size() != dto.getCertificati().size()) {
                bindingResult.rejectValue("images", "mismatch",
                        "Il numero di immagini deve coincidere con il numero di certificati");
            }
        }

        // Controllo globale se ci sono errori di binding
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
                logger.error("Field error in PacchettoCreationDTO => field '{}' : {}",
                        error.getField(), error.getDefaultMessage());
            });
            // Ritorno errori se presenti
            return ResponseEntity.badRequest().body(errors);
        }

        // Prosegui con la creazione
        try {
            pacchettoService.createPacchetto(
                    dto.getName(),
                    dto.getPrice(),
                    dto.getCategory(),
                    dto.getInfo(),
                    dto.getAvailability(),
                    dto.getImages(),       // Lista di immagini
                    dto.getCertificati(),  // Lista di certificati
                    staff.toUpperCase()
            );
            logger.info("Pacchetto creato con successo.");
            return ResponseEntity.ok("Pacchetto creato con successo.");
        } catch (Exception e) {
            logger.error("Errore durante la creazione del pacchetto", e);
            return ResponseEntity.status(500).body(
                    Map.of("error", "Errore durante la creazione del pacchetto: " + e.getMessage())
            );
        }
    }

    /**
     * Recupera la lista di pacchetti con stato "In attesa di approvazione".
     * (Simile a getProdottiInAttesa in ProdottoController)
     */
    @GetMapping("/curatore/list")
    public ResponseEntity<List<Pacchetto>> getPacchettiPerCuratore() {
        try {
            List<Pacchetto> pacchetti = pacchettoService.getPacchettiInAttesa();
            return ResponseEntity.ok(pacchetti);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Azione del curatore per approvare o rimandare il pacchetto.
     */
    @PostMapping("/curatore/action")
    public ResponseEntity<String> azioneCuratore(
            @RequestParam Long pacchettoId,
            @RequestParam String action,
            @RequestParam(required = false) String curatorComments
    ) {
        try {
            pacchettoService.gestisciPacchetto(pacchettoId, action, curatorComments);
            if ("approva".equalsIgnoreCase(action)) {
                return ResponseEntity.ok("Pacchetto approvato con successo.");
            } else if ("rimanda".equalsIgnoreCase(action)) {
                return ResponseEntity.ok("Pacchetto rimandato al distributore.");
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
     * Recupera tutti i pacchetti con stato approvato.
     */
    @GetMapping("/approvati")
    public ResponseEntity<List<Pacchetto>> getAllPacchettiApprovati() {
        try {
            List<Pacchetto> pacchetti = pacchettoService.getPacchettiApprovati();
            return ResponseEntity.ok(pacchetti);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Pubblicazione del pacchetto nel "Marketplace".
     */
    @PostMapping("/{staff}/publish")
    public ResponseEntity<String> publishPacchetto(
            @PathVariable String staff,
            @RequestBody Map<String, Object> request
    ) {
        try {
            // 1) Recupera pacchettoId
            Long pacchettoId = request.get("pacchettoId") != null
                    ? Long.valueOf(request.get("pacchettoId").toString())
                    : null;
            if (pacchettoId == null) {
                return ResponseEntity.badRequest().body("Parametro 'pacchettoId' mancante.");
            }
            // 2) Recupera lista di shippingOptions
            @SuppressWarnings("unchecked")
            List<String> shippingOptions = (List<String>) request.get("shippingOptions");

            // 3) Chiamata al service
            pacchettoService.pubblicaPacchettoNelMarketplace(pacchettoId, shippingOptions);

            logger.info("Pacchetto con ID {} pubblicato nel Marketplace da '{}'. Opzioni di spedizione: {}",
                    pacchettoId, staff, shippingOptions);

            return ResponseEntity.ok("Pacchetto pubblicato nel Marketplace con successo.");
        } catch (ClassCastException e) {
            logger.error("Formato 'shippingOptions' non valido: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Formato 'shippingOptions' non valido.");
        } catch (IllegalArgumentException e) {
            logger.error("Errore durante la pubblicazione del pacchetto: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Errore interno durante la pubblicazione del pacchetto: {}", e.getMessage());
            return ResponseEntity.status(500).body("Errore interno del server: " + e.getMessage());
        }
    }

    /**
     * Recupera i pacchetti "Rimandati" per un determinato staff (es. DISTRIBUTORE).
     */
    @GetMapping("/{staff}/rimandati")
    public ResponseEntity<List<Pacchetto>> getPacchettiRimandatiPerStaff(@PathVariable String staff) {
        try {
            List<Pacchetto> pacchetti = pacchettoService.getPacchettiRimandatiPerStaff(staff.toUpperCase());
            return ResponseEntity.ok(pacchetti);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Recupera tutti i pacchetti "Approvati" per il ruolo specificato.
     */
    @GetMapping("/{staff}/approvati")
    public ResponseEntity<List<Pacchetto>> getPacchettiApprovatiPerStaff(@PathVariable String staff) {
        try {
            List<Pacchetto> pacchetti = pacchettoService.getPacchettiApprovatiPerStaff(staff.toUpperCase());
            return ResponseEntity.ok(pacchetti);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Rispedisce al curatore un pacchetto in precedenza rimandato.
     */
    @PostMapping("/{staff}/resend")
    public ResponseEntity<String> resendPacchetto(
            @PathVariable String staff,
            @RequestBody PacchettoRimandatoRequest request
    ) {
        try {
            Long pacchettoId = request.getPacchettoId();
            pacchettoService.resendPacchetto(pacchettoId, staff.toUpperCase());
            return ResponseEntity.ok("Pacchetto inviato nuovamente al curatore.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore durante l'invio del pacchetto: " + e.getMessage());
        }
    }

    /**
     * Aggiorna i dettagli di un pacchetto.
     * Esempio di endpoint => /api/pacchetti/DISTRIBUTORE/update?id=...
     */
    @PostMapping("/{staff}/update")
    public ResponseEntity<String> updatePacchetto(
            @PathVariable String staff,
            @RequestParam Long id,

            // Campi testuali
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String info,
            @RequestParam(required = false) Integer availability,

            // File upload multipli: immagini e certificati (liste)
            @RequestParam(required = false) List<MultipartFile> images,
            @RequestParam(required = false) List<MultipartFile> certificati
    ) {
        try {
            // Se passi la logica di verifica corrispondenza dimensioni (images.size == certificati.size)
            // dovrai farlo qui (o nel service).
            if (images != null && certificati != null && images.size() != certificati.size()) {
                return ResponseEntity.badRequest().body("Numero di immagini e certificati non corrispondono.");
            }

            pacchettoService.updatePacchetto(
                    id, name, price, category, info, availability,
                    images, certificati, staff.toUpperCase()
            );
            return ResponseEntity.ok("Pacchetto aggiornato con successo.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante l'aggiornamento del pacchetto: " + e.getMessage());
        }
    }

    /**
     * Restituisce la lista dei pacchetti con stato "pubblicato".
     * Questi pacchetti appaiono nel "Marketplace".
     */
    @GetMapping("/published")
    public ResponseEntity<List<Pacchetto>> getPacchettiPubblicati() {
        try {
            List<Pacchetto> pacchetti = pacchettoService.getPacchettiByStato("pubblicato");
            return ResponseEntity.ok(pacchetti);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
