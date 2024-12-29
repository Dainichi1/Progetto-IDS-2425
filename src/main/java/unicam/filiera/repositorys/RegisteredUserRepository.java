package unicam.filiera.repositorys;

import unicam.filiera.models.actors.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.filiera.models.roles.Role;

import java.util.Optional;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<RegisteredUser> findByUsernameAndRole(String username, Role role);
    Optional<RegisteredUser> findByUsername(String username);

}
