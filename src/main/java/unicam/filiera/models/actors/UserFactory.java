package unicam.filiera.models.actors;

import unicam.filiera.models.roles.Role;

public class UserFactory {

    public static User createUser(String userType) {
        switch (userType.toUpperCase()) {
            case "GENERIC":
                return new GenericUser();
            case "STAFF":
                return new StaffUser();
            default:
                throw new IllegalArgumentException("Tipo di utente non valido: " + userType);
        }
    }
}
