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
@RequestMapping("/api/distributors")
public class DistributorController {

    @Autowired
    private RegisteredUserService registeredUserService;

    @GetMapping("/list")
    public ResponseEntity<List<RegisteredUser>> listDistributors() {
        try {
            List<RegisteredUser> distributors = registeredUserService.getUsersByRole("DISTRIBUTORE_TIPICITA");
            return ResponseEntity.ok(distributors);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
