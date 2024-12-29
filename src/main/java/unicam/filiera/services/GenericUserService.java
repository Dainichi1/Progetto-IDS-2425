package unicam.filiera.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.filiera.models.actors.GenericUser;
import unicam.filiera.models.actors.RegisteredUser;
import unicam.filiera.models.roles.Role;
import unicam.filiera.repositorys.GenericUserRepository;
import unicam.filiera.repositorys.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class GenericUserService {

    @Autowired
    private GenericUserRepository genericUserRepository;

    @Autowired
    private UserRepository userRepository;

    // Metodo per ottenere tutti gli utenti generici
    public List<GenericUser> getAllGenericUsers() {
        return genericUserRepository.findAll();
    }

    /**
     * Crea un nuovo utente generico con i campi obbligatori.
     *
     * @param address     Indirizzo dell'utente generico.
     * @param dateOfBirth Data di nascita dell'utente generico.
     * @param lastname    Cognome dell'utente generico.
     * @param name        Nome dell'utente generico.
     */
    public void createGenericUser(String address, LocalDate dateOfBirth, String lastname, String name) {
        // Valida i campi obbligatori
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("L'indirizzo non può essere vuoto.");
        }
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("La data di nascita non può essere vuota.");
        }
        if (lastname == null || lastname.isBlank()) {
            throw new IllegalArgumentException("Il cognome non può essere vuoto.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto.");
        }

        // Crea un nuovo utente generico
        GenericUser genericUser = new GenericUser();
        genericUser.setAddress(address);
        genericUser.setDateOfBirth(dateOfBirth);
        genericUser.setLastname(lastname);
        genericUser.setName(name);

        // Imposta valori predefiniti
        genericUser.setEmail(null);       // Email sarà null nella fase di creazione
        genericUser.setPassword(null);    // Password sarà null nella fase di creazione
        genericUser.setUsername(null);    // Username sarà null nella fase di creazione
        genericUser.setRole(Role.UTENTE_GENERICO); // Ruolo di default

        // Salva l'utente nella tabella GENERIC_USER
        genericUserRepository.save(genericUser);
    }

    public void promoteGenericUserToRegisteredUser(Long genericUserId, String username, String password, String email) {
        GenericUser genericUser = genericUserRepository.findById(genericUserId)
                .orElseThrow(() -> new IllegalArgumentException("Utente generico non trovato."));

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("L'username è già in uso.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("L'email è già in uso.");
        }

        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setName(genericUser.getName());
        registeredUser.setLastname(genericUser.getLastname());
        registeredUser.setAddress(genericUser.getAddress());
        registeredUser.setDateOfBirth(genericUser.getDateOfBirth());
        registeredUser.setUsername(username);
        registeredUser.setPassword(password);
        registeredUser.setEmail(email);
        registeredUser.setRole(Role.ACQUIRENTE);
        registeredUser.setAmount(0.0);

        userRepository.save(registeredUser);
        genericUserRepository.delete(genericUser);
    }

}
