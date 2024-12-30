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
@RequestMapping("/api/animators")
public class AnimatorController {

    @Autowired
    private RegisteredUserService registeredUserService;

    @GetMapping("/list")
    public ResponseEntity<List<RegisteredUser>> listAnimators() {
        try {
            List<RegisteredUser> animators = registeredUserService.getUsersByRole("ANIMATORE_FILIERA");
            return ResponseEntity.ok(animators);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
