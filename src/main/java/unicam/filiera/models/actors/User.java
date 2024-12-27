package unicam.filiera.models.actors;

import unicam.filiera.dtos.ProfileDetails;
import unicam.filiera.models.roles.Role;
import jakarta.persistence.*;
import java.time.LocalDate;

@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String lastname;
    private String username;

    @Column(unique = true, nullable = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String address;

    private LocalDate dateOfBirth;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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


    public boolean login() {
        // Implementazione del metodo di login
        return true;
    }

    public void logout() {
        // Implementazione del metodo di logout
    }

    public boolean updateProfile(ProfileDetails details) {
        if(details.getName() != null) this.name = details.getName();
        if(details.getLastname() != null) this.lastname = details.getLastname();
        if(details.getUsername() != null) this.username = details.getUsername();
        if(details.getEmail() != null) this.email = details.getEmail();
        if(details.getAddress() != null) this.address = details.getAddress();
        if(details.getDateOfBirth() != null) this.dateOfBirth = details.getDateOfBirth();
        return true;
    }
}
