package unicam.filiera.restController;

import jakarta.validation.Valid;
import unicam.filiera.dtos.StaffUserCreationDTO;
import unicam.filiera.dtos.StaffUserRegistrationDTO;
import unicam.filiera.models.actors.RegisteredUser;
import unicam.filiera.models.actors.StaffUser;
import unicam.filiera.models.actors.UserFactory;
import unicam.filiera.models.roles.Role;
import unicam.filiera.repositorys.StaffUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unicam.filiera.services.StaffUserService;

@RestController
@RequestMapping("/api/staff-user")
public class StaffUserController {

    private static final Logger logger = LoggerFactory.getLogger(StaffUserController.class);

    @Autowired
    private StaffUserService staffUserService;

    @Autowired
    private StaffUserRepository staffUserRepository;

    @GetMapping("/list")
    public ResponseEntity<List<StaffUser>> listStaffUsers() {
        logger.info("Richiesta per elencare gli StaffUser");
        List<StaffUser> users = staffUserService.getAllStaffUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createStaffUser(@Valid @RequestBody StaffUserCreationDTO creationDTO) {
        logger.info("Ricevuta richiesta di creazione StaffUser: {}", creationDTO);
        StaffUser staffUser = (StaffUser) UserFactory.createUser("STAFF");
        staffUser.setName(creationDTO.getName());
        staffUser.setLastname(creationDTO.getLastname());
        staffUser.setAddress(creationDTO.getAddress());
        staffUser.setDateOfBirth(creationDTO.getDateOfBirth());
        staffUser.setRole(creationDTO.getRole());

        try {
            staffUserService.saveStaffUser(staffUser);
            logger.info("StaffUser creato con successo: {}", staffUser);
            return ResponseEntity.ok("StaffUser creato con successo. Codice: " + staffUser.getCodice());
        } catch (Exception e) {
            logger.error("Errore durante la creazione dello StaffUser", e);
            throw e;
        }
    }

    @PostMapping("/promote")
    public ResponseEntity<String> promoteStaffUser(@Valid @RequestBody StaffUserRegistrationDTO dto) {
        try {
            // 1. Trova StaffUser by codice
            // 2. Crea e salva RegisteredUser
            // 3. Elimina StaffUser
            staffUserService.promoteStaffToRegistered(dto);
            return ResponseEntity.ok("StaffUser promosso a RegisteredUser con successo!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        }
    }
}

