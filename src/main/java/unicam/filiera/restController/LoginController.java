package unicam.filiera.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unicam.filiera.models.actors.RegisteredUser;
import unicam.filiera.models.roles.Role;
import unicam.filiera.repositorys.RegisteredUserRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private RegisteredUserRepository registeredUserRepository;

    @PostMapping("/buyer")
    public ResponseEntity<String> loginBuyer(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        // Cerca un utente con il ruolo ACQUIRENTE
        RegisteredUser user = registeredUserRepository.findByUsernameAndRole(username, Role.ACQUIRENTE)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato o ruolo non valido."));

        // Verifica la password
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password errata.");
        }

        return ResponseEntity.ok("Login avvenuto con successo.");
    }
}
