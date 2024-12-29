package unicam.filiera.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.filiera.models.actors.GenericUser;

@Repository
public interface GenericUserRepository extends JpaRepository<GenericUser, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
