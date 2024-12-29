package unicam.filiera.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.filiera.dtos.BuyerRegistrationDTO;
import unicam.filiera.models.actors.GenericUser;
import unicam.filiera.services.GenericUserService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/generic-user")
public class GenericUserController {

    @Autowired
    private GenericUserService genericUserService;

    // Endpoint per ottenere la lista degli utenti generici
    @GetMapping("/list")
    public ResponseEntity<List<GenericUser>> listGenericUsers() {
        try {
            List<GenericUser> genericUsers = genericUserService.getAllGenericUsers();
            return ResponseEntity.ok(genericUsers);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Restituisce errore 500 in caso di problemi
        }
    }

    // Endpoint per creare un nuovo utente generico
    @PostMapping("/create")
    public ResponseEntity<String> createGenericUser(@Valid @RequestBody BuyerRegistrationDTO buyerRegistrationDTO) {
        try {
            // Converte la stringa della data in LocalDate
            LocalDate dateOfBirth = LocalDate.parse(buyerRegistrationDTO.getDateOfBirth());

            // Chiama il servizio per creare l'utente generico
            genericUserService.createGenericUser(
                    buyerRegistrationDTO.getAddress(),
                    dateOfBirth,
                    buyerRegistrationDTO.getLastname(),
                    buyerRegistrationDTO.getName()
            );
            return ResponseEntity.ok("Utente generico creato con successo.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        }
    }

    // Endpoint per promuovere un utente generico
    @PostMapping("/promote")
    public ResponseEntity<String> promoteGenericUser(@Valid @RequestBody BuyerRegistrationDTO buyerRegistrationDTO) {
        try {
            genericUserService.promoteGenericUserToRegisteredUser(
                    buyerRegistrationDTO.getGenericUserId(),
                    buyerRegistrationDTO.getUsername(),
                    buyerRegistrationDTO.getPassword(),
                    buyerRegistrationDTO.getEmail()
            );
            return ResponseEntity.ok("Utente generico promosso a utente registrato con successo.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        }
    }
}
