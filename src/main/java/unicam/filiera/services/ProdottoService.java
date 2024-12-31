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
    private static final String BASE_UPLOAD_DIR = "E:\\Progetto-IDS-2425\\uploads";

    @Autowired
    private ProdottoRepository prodottoRepository;

    public List<Prodotto> getProdottiApprovati() {
        return prodottoRepository.findByStato("Approvato");
    }


    /**
     * Crea un nuovo prodotto utilizzando il Factory Pattern.
     * Salva immagini e certificati, assegna lo stato iniziale, e persiste il prodotto nel database.
     *
     * @param name         Nome del prodotto
     * @param price        Prezzo del prodotto
     * @param category     Categoria del prodotto
     * @param info         Informazioni aggiuntive
     * @param availability Quantità disponibile
     * @param images       Immagine del prodotto
     * @param certificato  Certificato del prodotto
     * @param staff
     * @throws Exception Se si verifica un errore durante il salvataggio
     */
    public void createProdotto(String name, Double price, String category, String info, Integer availability,
                               MultipartFile images, MultipartFile certificato, String staff) throws Exception {


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
                .staff(staff)
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
     * Pubblica un prodotto nel marketplace.
     *
     * @param prodottoId ID del prodotto da pubblicare
     * @throws IllegalArgumentException se il prodotto non esiste o non è approvato
     */
    public void pubblicaProdottoNelMarketplace(Long prodottoId) {
        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con ID: " + prodottoId));

        if (!"approvato".equalsIgnoreCase(prodotto.getStato())) {
            throw new IllegalArgumentException("Solo i prodotti approvati possono essere pubblicati nel marketplace.");
        }

        // Imposta lo stato a "pubblicato"
        prodotto.setStato("pubblicato");
        prodottoRepository.save(prodotto);
    }

    /**
     * Recupera tutti i prodotti con stato "Approvato".
     *
     * @return Lista dei prodotti approvati
     */
    public List<Prodotto> getProdottiRimandatiPerStaff(String staff) {
        return prodottoRepository.findByStatoAndStaff("Rimandato", staff);
    }

    public List<Prodotto> getProdottiApprovatiPerStaff(String staff) {
        return prodottoRepository.findByStatoAndStaff("Approvato", staff);
    }

    public void resendProdotto(Long prodottoId, String staff) {
        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con ID: " + prodottoId));

        if (!prodotto.getStaff().equalsIgnoreCase(staff)) {
            throw new IllegalArgumentException("Il prodotto non appartiene al ruolo specificato.");
        }

        prodotto.setStato("In attesa di approvazione");
        prodotto.setCuratorComments(null); // Rimuove eventuali commenti
        prodottoRepository.save(prodotto);
    }


    public void updateProdotto(
            Long id,
            String name,
            Double price,
            String category,
            String info,
            Integer availability,
            MultipartFile images,
            MultipartFile certificato,
            String staff
    ) throws Exception {

        Prodotto prodotto = prodottoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con ID: " + id));


        // Aggiorna i campi testuali se forniti
        if (name != null) prodotto.setName(name);
        if (price != null) prodotto.setPrice(BigDecimal.valueOf(price));
        if (category != null) prodotto.setCategory(category);
        if (info != null) prodotto.setInfo(info);
        if (availability != null) prodotto.setAvailability(availability);
        // Salvataggio immagine se fornita
        if (images != null && !images.isEmpty()) {
            String imagePath = saveFile(images, "images");
            prodotto.setImages(imagePath);
        }
        // Salvataggio certificato se fornito
        if (certificato != null && !certificato.isEmpty()) {
            String certPath = saveFile(certificato, "certificati");
            prodotto.setCertificato(certPath);
        }
        // Salva le modifiche nel DB
        prodottoRepository.save(prodotto);
    }



}
