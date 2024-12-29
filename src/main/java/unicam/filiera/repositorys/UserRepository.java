package unicam.filiera.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.filiera.models.actors.RegisteredUser;
import unicam.filiera.models.roles.Role;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<RegisteredUser, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<RegisteredUser> findByRole(Role role);

}
