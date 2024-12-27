package unicam.filiera.models.actors;

import unicam.filiera.dtos.ProfileDetails;
import unicam.filiera.models.roles.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "generic_user")
public class GenericUser extends User {





    public void viewProducts() {
    }



    // Override del metodo 'updateProfile' se necessario
    @Override
    public boolean updateProfile(ProfileDetails details) {
        return super.updateProfile(details);
    }
}
