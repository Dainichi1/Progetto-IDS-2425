package unicam.filiera.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.filiera.models.actors.RegisteredUser;
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
}
