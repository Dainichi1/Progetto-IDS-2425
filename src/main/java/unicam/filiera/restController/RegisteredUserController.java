package unicam.filiera.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.filiera.dtos.BuyerRegistrationDTO;
import unicam.filiera.services.RegisteredUserService;

@RestController
@RequestMapping("/api/registered-user")
public class RegisteredUserController {

    @Autowired
    private RegisteredUserService registeredUserService;

    @PostMapping("/create")
    public ResponseEntity<String> createRegisteredUser(@RequestBody BuyerRegistrationDTO buyerRegistrationDTO) {
        try {
            // Chiama il servizio per creare l'utente
            registeredUserService.registerBuyer(buyerRegistrationDTO);
            return ResponseEntity.ok("Registrazione completata con successo.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        }
    }
}
