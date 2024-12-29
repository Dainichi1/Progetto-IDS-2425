package unicam.filiera.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.filiera.dtos.BuyerRegistrationDTO;
import unicam.filiera.models.actors.RegisteredUser;
import unicam.filiera.models.roles.Role;
import unicam.filiera.repositorys.UserRepository;

import java.util.List;

@Service
public class RegisteredUserService {

    @Autowired
    private UserRepository userRepository;

    // Metodo per registrare un nuovo acquirente
    public void registerBuyer(BuyerRegistrationDTO buyerRegistrationDTO) {
        // Controlla se l'username o l'email sono già esistenti
        if (userRepository.existsByUsername(buyerRegistrationDTO.getUsername())) {
            throw new IllegalArgumentException("Username già esistente.");
        }
        if (userRepository.existsByEmail(buyerRegistrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email già esistente.");
        }

        // Crea un nuovo utente registrato
        RegisteredUser user = new RegisteredUser();
        user.setUsername(buyerRegistrationDTO.getUsername());
        user.setPassword(buyerRegistrationDTO.getPassword());
        user.setEmail(buyerRegistrationDTO.getEmail());
        user.setRole(Role.ACQUIRENTE); // Imposta il ruolo come ACQUIRENTE
        user.setAmount(0.0); // Imposta un valore di default

        // Salva l'utente nella tabella REGISTERED_USER
        userRepository.save(user);
    }

    // Metodo per ottenere tutti gli acquirenti
    public List<RegisteredUser> getAllBuyers() {
        // Filtra gli utenti con il ruolo ACQUIRENTE
        return userRepository.findByRole(Role.ACQUIRENTE);
    }
}
