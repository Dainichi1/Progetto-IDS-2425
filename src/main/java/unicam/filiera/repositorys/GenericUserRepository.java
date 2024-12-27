package unicam.filiera.repositorys;

import unicam.filiera.models.actors.GenericUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenericUserRepository extends JpaRepository<GenericUser, Long> {
}
