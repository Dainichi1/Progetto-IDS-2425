package unicam.filiera.restController;

import jakarta.validation.Valid;
import unicam.filiera.dtos.GenericUserCreationDTO;
import unicam.filiera.models.actors.GenericUser;
import unicam.filiera.models.roles.Role;
import unicam.filiera.repositorys.GenericUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/generic-user")
public class GenericUserController {

    private static final Logger logger = LoggerFactory.getLogger(GenericUserController.class);

    @Autowired
    private GenericUserRepository genericUserRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createGenericUser(@Valid @RequestBody GenericUserCreationDTO creationDTO) {
        logger.info("Ricevuta richiesta di creazione Utente Generico: {}", creationDTO);

        // Crea un nuovo GenericUser
        GenericUser genericUser = new GenericUser();
        genericUser.setName(creationDTO.getName());
        genericUser.setLastname(creationDTO.getLastname());
        genericUser.setAddress(creationDTO.getAddress());
        genericUser.setDateOfBirth(creationDTO.getDateOfBirth());

        // Imposta ruolo
        genericUser.setRole(Role.UTENTE_GENERICO);
        // 'code' non è presente in GenericUser, nessuna azione necessaria

        try {
            // Salva l'utente nel repository
            genericUserRepository.save(genericUser);
            logger.info("Utente Generico creato con successo: {}", genericUser);
            return ResponseEntity.ok("Utente Generico creato con successo.");
        } catch (Exception e) {
            logger.error("Errore durante la creazione dell'Utente Generico", e);
            throw e; // L'eccezione verrà gestita dal GlobalExceptionHandler
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<GenericUser>> listGenericUsers() {
        logger.info("Ricevuta richiesta per elencare gli Utenti Generici");
        List<GenericUser> users = genericUserRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
