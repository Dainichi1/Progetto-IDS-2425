package unicam.filiera.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.NotBlank;

public class BuyerRegistrationDTO {

    private Long genericUserId;

    @NotBlank(message = "Il nome utente non può essere vuoto.")
    private String username;

    private String password;

    private String email;

    @NotBlank(message = "L'indirizzo non può essere vuoto.")
    private String address;

    @NotBlank(message = "Il cognome non può essere vuoto.")
    private String lastname;

    @NotBlank(message = "Il nome non può essere vuoto.")
    private String name;

    @NotBlank(message = "La data di nascita non può essere vuota.")
    private String dateOfBirth;

    // Getter e Setter
    public Long getGenericUserId() {
        return genericUserId;
    }

    public void setGenericUserId(Long genericUserId) {
        this.genericUserId = genericUserId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}

