package unicam.filiera.models.actors;

import jakarta.persistence.Id;
import unicam.filiera.models.roles.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "registered_user")
public class RegisteredUser extends User {

    @Id
    public String code;

    // Getters and Setters

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}

