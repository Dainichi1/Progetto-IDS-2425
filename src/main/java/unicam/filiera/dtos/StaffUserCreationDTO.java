package unicam.filiera.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import unicam.filiera.models.roles.Role;
import java.time.LocalDate;


public class StaffUserCreationDTO {

    @NotBlank(message = "Il nome è obbligatorio.")
    private String name;

    @NotBlank(message = "Il cognome è obbligatorio.")
    private String lastname;

    @NotBlank(message = "L'indirizzo è obbligatorio.")
    private String address;

    @NotNull(message = "La data di nascita è obbligatoria.")
    private LocalDate dateOfBirth;

    @NotNull(message = "Il ruolo è obbligatorio.")
    private Role role;

    // Getters e Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
