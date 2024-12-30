package unicam.filiera.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.filiera.models.Prodotto;

import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

    // Trova i prodotti in base allo stato
    List<Prodotto> findByStato(String stato);

    // Trova i prodotti approvati
    default List<Prodotto> findApprovedProducts() {
        return findByStato("approvato");
    }

    // Trova i prodotti pendenti
    default List<Prodotto> findPendingProducts() {
        return findByStato("pendente");
    }

    // Trova i prodotti rifiutati
    default List<Prodotto> findRejectedProducts() {
        return findByStato("rifiutato");
    }

    // Trova prodotti per categoria
    List<Prodotto> findByCategory(String category);

    // Trova prodotti per disponibilità maggiore di un valore specifico
    List<Prodotto> findByAvailabilityGreaterThan(int availability);

    // Trova prodotti in base al nome (cerca case insensitive)
    List<Prodotto> findByNameIgnoreCaseContaining(String name);
}
