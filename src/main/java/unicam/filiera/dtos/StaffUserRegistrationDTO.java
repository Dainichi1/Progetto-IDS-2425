package unicam.filiera.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StaffUserRegistrationDTO {

    @NotBlank(message = "Il codice è obbligatorio.")
    private String codice;

    @NotBlank(message = "Username è obbligatorio.")
    private String username;

    @NotBlank(message = "Password è obbligatoria.")
    private String password;

    @NotBlank(message = "Email è obbligatoria.")
    private String email;

    @NotNull(message = "Lo staffUserId non può essere nullo.")
    private Long staffUserId;


    // Getters e Setters
    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
