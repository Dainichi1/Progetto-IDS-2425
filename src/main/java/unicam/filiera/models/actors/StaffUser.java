package unicam.filiera.models.actors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Random;

@Entity
@Table(name = "staff_users")
public class StaffUser extends User {

    @Column(unique = true, nullable = false, length = 4)
    private String codice;

    // Costruttore
    public StaffUser() {
        this.codice = generateRandomCode();
    }

    // Getter e Setter
    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    // Metodo per generare un codice di 4 lettere casuali
    private String generateRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rng = new Random();
        StringBuilder sb = new StringBuilder(4);
        for(int i = 0; i < 4; i++) {
            sb.append(characters.charAt(rng.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
