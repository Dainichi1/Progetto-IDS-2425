package unicam.filiera.models.actors;

import jakarta.persistence.*;
import unicam.filiera.models.roles.Role;

import java.util.Random;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Strategia di ereditarietà
@Table(name = "registered_user")
public  class RegisteredUser extends User {

    @Column(unique = true, length = 4) // Consente valori NULL
    private String code;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Double amount = 0.0;


    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true, nullable = true)
    private String email;

    @PrePersist
    @PreUpdate
    public void validateAndGenerateCodeForRole() {
        if (this.role != null && this.role != Role.ACQUIRENTE) {
            if (this.code == null || this.code.isEmpty()) {
                this.code = generateRandomCode();
            }
        }
    }

    private String generateRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(4);
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    // Getter e Setter
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // Se usi l'email, aggiungi getter e setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
