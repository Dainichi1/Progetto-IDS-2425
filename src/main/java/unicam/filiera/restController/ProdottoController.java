package unicam.filiera.restController;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unicam.filiera.dtos.ProdottoCreationDTO;
import unicam.filiera.models.Prodotto;
import unicam.filiera.request.ProdottoRimandatoRequest;
import unicam.filiera.services.ProdottoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prodotti")
public class ProdottoController {

    @Autowired
    private ProdottoService prodottoService;
    private static final Logger logger = LoggerFactory.getLogger(ProdottoController.class);

    /**
     * Crea un nuovo prodotto e lo invia al curatore per l'approvazione.
     */
    @PostMapping(value = "/{staff}/create", consumes = {"multipart/form-data"})
    public ResponseEntity<Object> createProdottoForStaff(
            @PathVariable String staff,
            @Valid @ModelAttribute ProdottoCreationDTO dto,
            BindingResult bindingResult
    ) {
        Map<String, String> errors = new HashMap<>();

        // Log dei valori ricevuti
        logger.info("Received DTO: name='{}', price='{}', category='{}', info='{}', availability='{}', images='{}', certificato='{}'",
                dto.getName(), dto.getPrice(), dto.getCategory(), dto.getInfo(), dto.getAvailability(),
                dto.getImages() != null ? dto.getImages().getOriginalFilename() : "null",
                dto.getCertificato() != null ? dto.getCertificato().getOriginalFilename() : "null"
        );

        // Verifica se l'immagine è presente
        if (dto.getImages() == null || dto.getImages().isEmpty()) {
            bindingResult.rejectValue("images", "NotEmpty", "L'immagine del prodotto è obbligatoria.");
        }

        // Verifica se il certificato è presente
        if (dto.getCertificato() == null || dto.getCertificato().isEmpty()) {
            bindingResult.rejectValue("certificato", "NotEmpty", "Il certificato è obbligatorio.");
        }

        // Verifica errori di validazione del DTO
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
                logger.error("Field error in object '{}' on field '{}': {}", error.getObjectName(), error.getField(), error.getDefaultMessage());
            });
        }

        // Se ci sono errori, restituisci gli errori in formato JSON
        if (!errors.isEmpty()) {
            logger.info("Validation failed with errors: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            prodottoService.createProdotto(
                    dto.getName(),
                    dto.getPrice(),
                    dto.getCategory(),
                    dto.getInfo(),
                    dto.getAvailability(),
                    dto.getImages(),
                    dto.getCertificato(),
                    staff.toUpperCase()
            );
            logger.info("Prodotto creato con successo.");
            return ResponseEntity.ok("Prodotto creato con successo.");
        } catch (Exception e) {
            logger.error("Errore durante la creazione del prodotto", e);
            return ResponseEntity.status(500).body(Map.of("error", "Errore durante la creazione del prodotto: " + e.getMessage()));
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
    @PostMapping("/{staff}/update")
    public ResponseEntity<String> updateProdotto(
            @PathVariable String staff,
            @RequestParam Long id,

            // Campi testuali
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String info,
            @RequestParam(required = false) Integer availability,

            // File upload
            @RequestParam(required = false) MultipartFile images,
            @RequestParam(required = false) MultipartFile certificato
    ) {
        try {
            // Passa tutto al Service
            prodottoService.updateProdotto(
                    id, name, price, category, info, availability,
                    images, certificato, staff.toUpperCase()
            );
            return ResponseEntity.ok("Prodotto aggiornato con successo.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante l'aggiornamento del prodotto: " + e.getMessage());
        }
    }



}