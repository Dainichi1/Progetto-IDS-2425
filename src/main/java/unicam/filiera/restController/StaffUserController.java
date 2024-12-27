package unicam.filiera.restController;

import jakarta.validation.Valid;
import unicam.filiera.dtos.StaffUserCreationDTO;
import unicam.filiera.models.actors.StaffUser;
import unicam.filiera.models.roles.Role;
import unicam.filiera.repositorys.StaffUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/staff-user")
public class StaffUserController {

    private static final Logger logger = LoggerFactory.getLogger(StaffUserController.class);

    @Autowired
    private StaffUserRepository staffUserRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createStaffUser(@Valid @RequestBody StaffUserCreationDTO creationDTO) {
        logger.info("Ricevuta richiesta di creazione StaffUser: {}", creationDTO);

        // Crea un nuovo StaffUser
        StaffUser staffUser = new StaffUser();
        staffUser.setName(creationDTO.getName());
        staffUser.setLastname(creationDTO.getLastname());
        staffUser.setAddress(creationDTO.getAddress());
        staffUser.setDateOfBirth(creationDTO.getDateOfBirth());
        staffUser.setRole(creationDTO.getRole());

        try {
            // Salva lo staff user nel repository
            staffUserRepository.save(staffUser);
            logger.info("StaffUser creato con successo: {}", staffUser);
            return ResponseEntity.ok("StaffUser creato con successo. Codice: " + staffUser.getCodice());
        } catch (Exception e) {
            logger.error("Errore durante la creazione dello StaffUser", e);
            throw e; // L'eccezione verrà gestita dal GlobalExceptionHandler
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<StaffUser>> listStaffUsers() {
        logger.info("Ricevuta richiesta per elencare gli StaffUser");
        List<StaffUser> users = staffUserRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
