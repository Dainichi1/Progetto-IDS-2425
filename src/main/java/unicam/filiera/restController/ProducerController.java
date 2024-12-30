package unicam.filiera.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.filiera.models.actors.RegisteredUser;
import unicam.filiera.services.RegisteredUserService;

import java.util.List;

@RestController
@RequestMapping("/api/producers")
public class ProducerController {

    @Autowired
    private RegisteredUserService registeredUserService;

    @GetMapping("/list")
    public ResponseEntity<List<RegisteredUser>> listProducers() {
        try {
            List<RegisteredUser> producers = registeredUserService.getUsersByRole("PRODUTTORE");
            return ResponseEntity.ok(producers);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
