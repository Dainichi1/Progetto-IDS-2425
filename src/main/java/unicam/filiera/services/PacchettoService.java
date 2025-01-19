package unicam.filiera.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unicam.filiera.models.Pacchetto;
import unicam.filiera.repositorys.PacchettoRepository;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class PacchettoService {

    // Percorso assoluto per i file di upload
    private static final String BASE_UPLOAD_DIR = "E:\\Progetto-IDS-2425\\uploads";

    @Autowired
    private PacchettoRepository pacchettoRepository;

    /**
     * Recupera i pacchetti con stato "Approvato".
     */
    public List<Pacchetto> getPacchettiApprovati() {
        return pacchettoRepository.findByStato("Approvato");
    }

    /**
     * Crea un nuovo pacchetto con più immagini e più certificati.
     * Assegna lo stato iniziale, e persiste il pacchetto nel database.
     */
    public void createPacchetto(String name,
                                Double price,
                                String category,
                                String info,
                                Integer availability,
                                List<MultipartFile> images,
                                List<MultipartFile> certificati,
                                String staff) throws Exception {

        // Salviamo le immagini
        List<String> imagePaths = saveMultipleFiles(images, "images");
        // Salviamo i certificati
        List<String> certPaths = saveMultipleFiles(certificati, "certificati");

        // Creiamo il Pacchetto usando il Builder
        Pacchetto pacchetto = new Pacchetto.Builder()
                .name(name)
                .price(BigDecimal.valueOf(price))
                .category(category)
                .info(info)
                .availability(availability)
                .images(String.join(",", imagePaths))      // Convertiamo la lista in CSV
                .certificati(String.join(",", certPaths))  // Convertiamo la lista in CSV
                .staff(staff)
                .build();

        pacchetto.setStato("In attesa di approvazione"); // stato iniziale

        pacchettoRepository.save(pacchetto);
    }

    /**
     * Recupera i pacchetti in attesa di approvazione.
     */
    public List<Pacchetto> getPacchettiInAttesa() {
        return pacchettoRepository.findByStato("In attesa di approvazione");
    }

    /**
     * Gestisce l'approvazione o il rimando di un pacchetto.
     */
    public void gestisciPacchetto(Long pacchettoId, String action, String curatorComments) {
        Pacchetto pacchetto = pacchettoRepository.findById(pacchettoId)
                .orElseThrow(() -> new IllegalArgumentException("Pacchetto non trovato con ID: " + pacchettoId));

        switch (action.toLowerCase()) {
            case "approva":
                pacchetto.setStato("Approvato");
                pacchetto.setCuratorComments(null);
                break;
            case "rimanda":
                pacchetto.setStato("Rimandato");
                pacchetto.setCuratorComments(curatorComments);
                break;
            default:
                throw new IllegalArgumentException("Azione non valida: " + action);
        }

        pacchettoRepository.save(pacchetto);
    }

    /**
     * Pubblica un pacchetto nel marketplace (stato = "pubblicato").
     */
    public void pubblicaPacchettoNelMarketplace(Long pacchettoId, List<String> shippingOptions) {
        Pacchetto pacchetto = pacchettoRepository.findById(pacchettoId)
                .orElseThrow(() -> new IllegalArgumentException("Pacchetto non trovato con ID: " + pacchettoId));

        if (!"approvato".equalsIgnoreCase(pacchetto.getStato())) {
            throw new IllegalArgumentException("Solo i pacchetti approvati possono essere pubblicati nel marketplace.");
        }

        // Calcolo delle opzioni e dei costi di spedizione
        String shippingOptionsString = String.join(",", shippingOptions);

        List<String> costs = new ArrayList<>();
        for (String option : shippingOptions) {
            switch (option) {
                case "ordinaria": costs.add("3"); break;
                case "corriere": costs.add("5"); break;
                case "espresso": costs.add("10"); break;
                default: break;
            }
        }
        String shippingCostCsv = String.join(",", costs);

        // Imposta le opzioni e i costi di spedizione nel pacchetto
        pacchetto.setShippingOptions(shippingOptionsString);
        pacchetto.setShippingCost(shippingCostCsv);

        // Imposta lo stato a "pubblicato"
        pacchetto.setStato("pubblicato");

        pacchettoRepository.save(pacchetto);
    }


    /**
     * Recupera i pacchetti "Rimandati" in base allo staff.
     */
    public List<Pacchetto> getPacchettiRimandatiPerStaff(String staff) {
        return pacchettoRepository.findByStatoAndStaff("Rimandato", staff);
    }

    /**
     * Recupera i pacchetti "Approvati" in base allo staff.
     */
    public List<Pacchetto> getPacchettiApprovatiPerStaff(String staff) {
        return pacchettoRepository.findByStatoAndStaff("Approvato", staff);
    }

    /**
     * Re-invia il pacchetto al curatore, rimettendolo "In attesa di approvazione".
     */
    public void resendPacchetto(Long pacchettoId, String staff) {
        Pacchetto pacchetto = pacchettoRepository.findById(pacchettoId)
                .orElseThrow(() -> new IllegalArgumentException("Pacchetto non trovato con ID: " + pacchettoId));

        if (!pacchetto.getStaff().equalsIgnoreCase(staff)) {
            throw new IllegalArgumentException("Il pacchetto non appartiene al ruolo specificato.");
        }

        pacchetto.setStato("In attesa di approvazione");
        pacchetto.setCuratorComments(null);
        pacchettoRepository.save(pacchetto);
    }

    /**
     * Aggiorna i dettagli di un pacchetto (campi testuali e file multipli).
     */
    public void updatePacchetto(Long id,
                                String name,
                                Double price,
                                String category,
                                String info,
                                Integer availability,
                                List<MultipartFile> images,
                                List<MultipartFile> certificati,
                                String staff) throws Exception {

        Pacchetto pacchetto = pacchettoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pacchetto non trovato con ID: " + id));

        // Campi testuali
        if (name != null) pacchetto.setName(name);
        if (price != null) pacchetto.setPrice(BigDecimal.valueOf(price));
        if (category != null) pacchetto.setCategory(category);
        if (info != null) pacchetto.setInfo(info);
        if (availability != null) pacchetto.setAvailability(availability);

        // Se ci sono nuove immagini
        if (images != null && !images.isEmpty()) {
            List<String> imagePaths = saveMultipleFiles(images, "images");
            pacchetto.setImages(String.join(",", imagePaths));
        }

        // Se ci sono nuovi certificati
        if (certificati != null && !certificati.isEmpty()) {
            List<String> certPaths = saveMultipleFiles(certificati, "certificati");
            pacchetto.setCertificati(String.join(",", certPaths));
        }

        pacchettoRepository.save(pacchetto);
    }

    /**
     * Recupera i pacchetti filtrati per stato (ignoreCase).
     */
    public List<Pacchetto> getPacchettiByStato(String stato) {
        return pacchettoRepository.findByStatoIgnoreCase(stato);
    }

    // --------------------------------------------------
    // Salvataggio multiplo di file sul file system.

    private List<String> saveMultipleFiles(List<MultipartFile> files, String subfolder) throws Exception {
        List<String> savedPaths = new ArrayList<>();
        if (files == null) return savedPaths;

        Path folderPath = Paths.get(BASE_UPLOAD_DIR, subfolder);
        Files.createDirectories(folderPath);

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String originalFileName = file.getOriginalFilename();
                String newFileName = System.currentTimeMillis() + "_" + originalFileName;
                Path filePath = folderPath.resolve(newFileName);
                file.transferTo(filePath.toFile());

                // Salva solo il percorso relativo
                savedPaths.add(subfolder + "/" + newFileName);
            }
        }
        return savedPaths;
    }
}
