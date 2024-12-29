package unicam.filiera.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.filiera.dtos.StaffUserRegistrationDTO;
import unicam.filiera.models.actors.RegisteredUser;
import unicam.filiera.models.actors.StaffUser;
import unicam.filiera.repositorys.RegisteredUserRepository;
import unicam.filiera.repositorys.StaffUserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StaffUserService {

    @Autowired
    private StaffUserRepository staffUserRepository;

    @Autowired
    private RegisteredUserRepository registeredUserRepository;

    public void promoteStaffToRegistered(StaffUserRegistrationDTO dto) {
        // 1. Recupera lo StaffUser in base al codice
        StaffUser staffUser = staffUserRepository.findByCodice(dto.getCodice())
                .orElseThrow(() -> new IllegalArgumentException(
                        "StaffUser non trovato con il codice: " + dto.getCodice()
                ));

        // 2. Verifica duplicati in REGISTERED_USERS
        if (registeredUserRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username già in uso: " + dto.getUsername());
        }
        if (registeredUserRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email già in uso: " + dto.getEmail());
        }

        // 3. Crea un RegisteredUser copiando i dati dallo StaffUser
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setName(staffUser.getName());
        registeredUser.setLastname(staffUser.getLastname());
        registeredUser.setAddress(staffUser.getAddress());
        registeredUser.setDateOfBirth(staffUser.getDateOfBirth());
        registeredUser.setRole(staffUser.getRole());

        registeredUser.setUsername(dto.getUsername());
        registeredUser.setPassword(dto.getPassword());
        registeredUser.setEmail(dto.getEmail());
        registeredUser.setAmount(0.0);

        registeredUser.setCode(staffUser.getCodice());

        // 4. Salva in REGISTERED_USERS
        registeredUserRepository.save(registeredUser);

        // 5. Elimina lo StaffUser da STAFF_USERS
        staffUserRepository.delete(staffUser);
    }

    public List<StaffUser> getAllStaffUsers() {
        // Recupera tutti gli StaffUser dalla tabella usando Spring Data JPA
        return staffUserRepository.findAll();
    }

    public void saveStaffUser(StaffUser staffUser) {
        // Salva lo StaffUser nel repository (creazione o aggiornamento)
        staffUserRepository.save(staffUser);
    }
}
