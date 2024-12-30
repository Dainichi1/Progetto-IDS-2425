package unicam.filiera.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unicam.filiera.models.Prodotto;
import unicam.filiera.repositorys.ProdottoRepository;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProdottoService {

    // Percorso assoluto per i file di upload
    private static final String BASE_UPLOAD_DIR = "C:\\Users\\Marco Torquati\\Desktop\\Progetto-IDS-2425\\uploads";

    @Autowired
    private ProdottoRepository prodottoRepository;

    /**
     * Crea un nuovo prodotto utilizzando il Factory Pattern.
     * Salva immagini e certificati, assegna lo stato iniziale, e persiste il prodotto nel database.
     *
     * @param name        Nome del prodotto
     * @param price       Prezzo del prodotto
     * @param category    Categoria del prodotto
     * @param info        Informazioni aggiuntive
     * @param availability Quantità disponibile
     * @param images      Immagine del prodotto
     * @param certificato Certificato del prodotto
     * @throws Exception Se si verifica un errore durante il salvataggio
     */
    public void createProdotto(String name, Double price, String category, String info, Integer availability,
                               MultipartFile images, MultipartFile certificato) throws Exception {

        // Logica di salvataggio file
        String imagePath = saveFile(images, "images");
        String certificatoPath = saveFile(certificato, "certificati");

        // Factory Pattern per creare un prodotto
        Prodotto prodotto = new Prodotto.Builder()
                .name(name)
                .price(BigDecimal.valueOf(price))
                .category(category)
                .info(info)
                .availability(availability)
                .images(imagePath)
                .certificato(certificatoPath)
                .build();

        prodotto.setStato("In attesa di approvazione"); // Stato iniziale

        // Salva il prodotto nel database
        prodottoRepository.save(prodotto);
    }

    /**
     * Recupera i prodotti in attesa di approvazione.
     *
     * @return Lista dei prodotti con stato "In attesa di approvazione"
     */
    public List<Prodotto> getProdottiInAttesa() {
        return prodottoRepository.findByStato("In attesa di approvazione");
    }

    /**
     * Gestisce l'approvazione o il rimando di un prodotto.
     *
     * @param prodottoId ID del prodotto
     * @param action     Azione da eseguire ("approva" o "rimanda")
     */
    public void gestisciProdotto(Long prodottoId, String action, String curatorComments) {
        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con ID: " + prodottoId));

        switch (action.toLowerCase()) {
            case "approva":
                prodotto.setStato("Approvato");
                prodotto.setCuratorComments(null); // Rimuove eventuali commenti
                break;
            case "rimanda":
                prodotto.setStato("Rimandato");
                prodotto.setCuratorComments(curatorComments); // Aggiunge il commento
                break;
            default:
                throw new IllegalArgumentException("Azione non valida: " + action);
        }

        prodottoRepository.save(prodotto);
    }

    /**
     * Salva un file nel percorso specificato.
     *
     * @param file       File da salvare
     * @param subfolder  Sottocartella di destinazione (es. "images" o "certificati")
     * @return Percorso del file salvato
     * @throws Exception Se si verifica un errore durante il salvataggio
     */
    private String saveFile(MultipartFile file, String subfolder) throws Exception {
        if (file == null || file.isEmpty()) {
            return null;
        }
        Path folderPath = Paths.get(BASE_UPLOAD_DIR, subfolder);
        Files.createDirectories(folderPath);

        String originalFileName = file.getOriginalFilename();
        String newFileName = System.currentTimeMillis() + "_" + originalFileName;

        Path filePath = folderPath.resolve(newFileName);
        file.transferTo(filePath.toFile());

        // Salva solo il percorso relativo
        return subfolder + "/" + newFileName;
    }



    /**
     * Recupera tutti i prodotti con stato "Approvato".
     *
     * @return Lista dei prodotti approvati
     */
    public List<Prodotto> getProdottiApprovati() {
        return prodottoRepository.findByStato("Approvato");
    }

    /**
     * Recupera tutti i prodotti con stato "Rimandato".
     *
     * @return Lista dei prodotti rimandati
     */
    public List<Prodotto> getProdottiRimandati() {
        return prodottoRepository.findByStato("Rimandato");
    }

    public void resendProdotto(Long prodottoId) {
        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con ID: " + prodottoId));

        prodotto.setStato("In attesa di approvazione");
        prodotto.setCuratorComments(null); // Rimuove eventuali commenti
        prodottoRepository.save(prodotto);
    }

    public void updateProdotto(Prodotto updatedProdotto) {
        Prodotto prodotto = prodottoRepository.findById(updatedProdotto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));

        // Aggiorna i campi
        prodotto.setName(updatedProdotto.getName());
        prodotto.setPrice(updatedProdotto.getPrice());
        prodotto.setCategory(updatedProdotto.getCategory());
        prodotto.setInfo(updatedProdotto.getInfo());
        prodotto.setAvailability(updatedProdotto.getAvailability());

        // Mantieni le immagini e i certificati se non forniti
        if (updatedProdotto.getImages() != null) {
            prodotto.setImages(updatedProdotto.getImages());
        }
        if (updatedProdotto.getCertificato() != null) {
            prodotto.setCertificato(updatedProdotto.getCertificato());
        }

        prodottoRepository.save(prodotto);
    }


}
