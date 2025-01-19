package unicam.filiera.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.filiera.models.Pacchetto;
import unicam.filiera.models.Prodotto;
import unicam.filiera.services.AcquistoService;
import unicam.filiera.services.ProdottoService;
import unicam.filiera.services.PacchettoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/acquisto")
public class AcquistoController {

    @Autowired
    private AcquistoService acquistoService;

    @Autowired
    private ProdottoService prodottoService;

    @Autowired
    private PacchettoService pacchettoService;

    /**
     * Endpoint per acquistare prodotti e pacchetti selezionati.
     * Si aspetta un JSON con due array: "prodottiIds" e "pacchettiIds".
     */
    @PostMapping("/conferma")
    public ResponseEntity<String> confermaAcquisto(@RequestBody Map<String, List<Long>> request) {
        List<Long> prodottiIds = request.get("prodottiIds");
        List<Long> pacchettiIds = request.get("pacchettiIds");

        List<Prodotto> prodottiDaAcquistare = new ArrayList<>();
        List<Pacchetto> pacchettiDaAcquistare = new ArrayList<>();

        if (prodottiIds != null) {
            for (Long id : prodottiIds) {
                prodottoService.getProdottiByStato("pubblicato").stream()
                        .filter(p -> p.getId().equals(id))
                        .findFirst()
                        .ifPresent(prodottiDaAcquistare::add);
            }
        }

        if (pacchettiIds != null) {
            for (Long id : pacchettiIds) {
                pacchettoService.getPacchettiByStato("pubblicato").stream()
                        .filter(p -> p.getId().equals(id))
                        .findFirst()
                        .ifPresent(pacchettiDaAcquistare::add);
            }
        }

        acquistoService.acquistaProdotti(prodottiDaAcquistare);
        acquistoService.acquistaPacchetti(pacchettiDaAcquistare);

        return ResponseEntity.ok("Acquisto confermato e registrato.");
    }
}
