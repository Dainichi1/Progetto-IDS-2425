package unicam.filiera.repositorys;

import unicam.filiera.models.actors.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
