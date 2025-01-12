package unicam.filiera.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.filiera.models.Pacchetto;

import java.util.List;

@Repository
public interface PacchettoRepository extends JpaRepository<Pacchetto, Long> {

    // Trova i pacchetti in base allo stato
    List<Pacchetto> findByStato(String stato);

    // Trova i pacchetti in stato "approvato"
    default List<Pacchetto> findApprovedPackages() {
        return findByStato("approvato");
    }

    // Trova i pacchetti in stato "pendente"
    default List<Pacchetto> findPendingPackages() {
        return findByStato("pendente");
    }

    // Trova i pacchetti in stato "rifiutato"
    default List<Pacchetto> findRejectedPackages() {
        return findByStato("rifiutato");
    }

    // Trova i pacchetti per categoria (se presente la logica di "category" in Pacchetto)
    List<Pacchetto> findByCategory(String category);

    // Trova i pacchetti per disponibilità maggiore di un valore specifico (se hai un campo availability)
    List<Pacchetto> findByAvailabilityGreaterThan(int availability);

    // Trova i pacchetti in base al nome (cerca case insensitive)
    // (se Pacchetto ha un campo "name" simile a Prodotto)
    List<Pacchetto> findByNameIgnoreCaseContaining(String name);

    // Trova pacchetti in base allo stato e allo staff (se hai un campo staff)
    List<Pacchetto> findByStatoAndStaff(String stato, String staff);

    // Trova pacchetti in base allo stato (case-insensitive)
    List<Pacchetto> findByStatoIgnoreCase(String stato);
}
