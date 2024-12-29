package unicam.filiera.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class GenericUserCreationDTO {

    @NotBlank(message = "L'indirizzo non può essere vuoto.")
    private String address;

    @NotNull(message = "La data di nascita non può essere vuota.")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Il cognome non può essere vuoto.")
    private String lastname;

    @NotBlank(message = "Il nome non può essere vuoto.")
    private String name;

    // Getter e Setter
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
}
