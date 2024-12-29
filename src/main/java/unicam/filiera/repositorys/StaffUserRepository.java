package unicam.filiera.repositorys;

import unicam.filiera.models.actors.StaffUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffUserRepository extends JpaRepository<StaffUser, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByCodice(String codice);

    Optional<StaffUser> findByCodice(String codice);


}
