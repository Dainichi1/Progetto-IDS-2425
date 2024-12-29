package unicam.filiera.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import unicam.filiera.models.actors.RegisteredUser;
import unicam.filiera.repositorys.RegisteredUserRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private RegisteredUserRepository registeredUserRepository;

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestParam String username) {
        RegisteredUser user = registeredUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        Map<String, String> userDetails = new HashMap<>();
        userDetails.put("name", user.getName());
        userDetails.put("lastname", user.getLastname());

        return ResponseEntity.ok(userDetails);
    }
}
