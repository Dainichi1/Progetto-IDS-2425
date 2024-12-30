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

    @PostMapping("/staff")
    public ResponseEntity<Map<String, String>> loginStaff(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        String roleString = loginData.get("role");
        String code = loginData.get("code");

        // Converte la stringa del ruolo in un oggetto Role
        Role role;
        try {
            role = Role.valueOf(roleString);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Ruolo non valido."));
        }

        // Cerca l'utente con il ruolo specifico
        RegisteredUser user = registeredUserRepository.findByUsernameAndRole(username, role)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato o ruolo non valido."));

        // Verifica la password
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Password errata."));
        }

        // Verifica il CODE (solo per ruoli specifici)
        if (user.getCode() == null || !user.getCode().equals(code)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Code errato."));
        }

        // Restituisci i dati necessari per il reindirizzamento
        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "role", user.getRole().name()
        ));
    }


}
