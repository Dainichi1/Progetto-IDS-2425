package unicam.filiera.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.filiera.dtos.BuyerRegistrationDTO;
import unicam.filiera.models.actors.RegisteredUser;
import unicam.filiera.models.roles.Role;
import unicam.filiera.repositorys.UserRepository;

import java.util.List;
import java.util.Optional;

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

    // Metodo per ottenere tutti gli utenti con un ruolo specifico
    public List<RegisteredUser> getUsersByRole(String roleName) {
        try {
            Role role = Role.valueOf(roleName); // Converte il nome del ruolo in un valore enum
            return userRepository.findByRole(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ruolo non valido: " + roleName);
        }
    }

    // Restituisce tutti gli acquirenti
    public List<RegisteredUser> getAllBuyers() {
        return getUsersByRole("ACQUIRENTE");
    }

    // 1) Metodo per trovare un RegisteredUser dato lo username
    public Optional<RegisteredUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 2) Metodo per aggiungere denaro al saldo di un buyer
    public String addAmountToBuyer(String username, Double amountToAdd) {
        Optional<RegisteredUser> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return "Utente non trovato.";
        }
        RegisteredUser user = userOpt.get();

        // Verifica il ruolo
        if (user.getRole() != Role.ACQUIRENTE) {
            return "Operazione non consentita: utente non è un ACQUIRENTE.";
        }
        // Aggiorna saldo
        double newAmount = user.getAmount() + amountToAdd;
        user.setAmount(newAmount);
        userRepository.save(user);

        return "Saldo aggiornato con successo. Nuovo saldo: " + newAmount;
    }
}
