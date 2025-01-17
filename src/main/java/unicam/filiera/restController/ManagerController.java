package unicam.filiera.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unicam.filiera.models.actors.RegisteredUser;
import unicam.filiera.services.RegisteredUserService;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    @Autowired
    private RegisteredUserService registeredUserService;

    @GetMapping("/list")
    public ResponseEntity<List<RegisteredUser>> listManagers() {
        try {
            List<RegisteredUser> managers = registeredUserService.getUsersByRole("GESTORE_PIATTAFORMA");
            return ResponseEntity.ok(managers);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}

