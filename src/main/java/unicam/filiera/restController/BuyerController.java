package unicam.filiera.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.filiera.models.actors.RegisteredUser;
import unicam.filiera.models.roles.Role;
import unicam.filiera.services.RegisteredUserService;

import java.util.List;

@RestController
@RequestMapping("/api/buyers")
public class BuyerController {

    @Autowired
    private RegisteredUserService registeredUserService;

    // Endpoint per ottenere la lista degli acquirenti
    @GetMapping("/list")
    public ResponseEntity<List<RegisteredUser>> listBuyers() {
        try {
            List<RegisteredUser> buyers = registeredUserService.getAllBuyers();
            return ResponseEntity.ok(buyers);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Restituisce errore 500 in caso di problemi
        }
    }

    @GetMapping("/{username}/amount")
    public ResponseEntity<?> getBuyerAmount(@PathVariable String username) {
        return registeredUserService.findByUsername(username)
                .map(user -> {
                    if (user.getRole() != Role.ACQUIRENTE) {
                        // Ritorna 403 senza body
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    }
                    // Ritorna 200 con body di tipo Double
                    return ResponseEntity.ok(user.getAmount());
                })
                // Se l'utente non esiste, 404
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * Aggiunge una somma di denaro al saldo di un Acquirente.
     *
     * Esempio richiesta: POST /api/buyers/pippo/addAmount?amountToAdd=50.0
     */
    @PostMapping("/{username}/addAmount")
    public ResponseEntity<String> addBuyerAmount(@PathVariable String username,
                                                 @RequestParam("amountToAdd") Double amountToAdd) {
        if (amountToAdd == null || amountToAdd <= 0) {
            return ResponseEntity.badRequest().body("L'importo da aggiungere deve essere > 0.");
        }

        // Chiama il service
        String responseMessage = registeredUserService.addAmountToBuyer(username, amountToAdd);

        // Gestione dei vari messaggi di risposta
        if (responseMessage.startsWith("Utente non trovato")) {
            return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
        } else if (responseMessage.startsWith("Operazione non consentita")) {
            return new ResponseEntity<>(responseMessage, HttpStatus.FORBIDDEN);
        }
        // Altrimenti, è andato tutto bene
        return ResponseEntity.ok(responseMessage);
    }
}
