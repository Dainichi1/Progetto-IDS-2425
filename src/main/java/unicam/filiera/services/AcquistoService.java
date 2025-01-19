package unicam.filiera.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.filiera.models.Pacchetto;
import unicam.filiera.models.PacchettoAcquistato;
import unicam.filiera.models.Prodotto;
import unicam.filiera.models.ProdottoAcquistato;
import unicam.filiera.repositorys.PacchettoAcquistatoRepository;
import unicam.filiera.repositorys.ProdottoAcquistatoRepository;

import java.util.List;

@Service
public class AcquistoService {

    @Autowired
    private ProdottoAcquistatoRepository prodottoAcquistatoRepository;

    @Autowired
    private PacchettoAcquistatoRepository pacchettoAcquistatoRepository;

    public void acquistaProdotto(Prodotto prodotto) {
        ProdottoAcquistato pa = new ProdottoAcquistato(
                prodotto.getCategory(),
                prodotto.getName(),
                prodotto.getPrice(),
                prodotto.getShippingOptions(),
                prodotto.getStaff(),
                prodotto.getShippingCost()
        );
        prodottoAcquistatoRepository.save(pa);
    }

    public void acquistaPacchetto(Pacchetto pacchetto) {
        PacchettoAcquistato pa = new PacchettoAcquistato(
                pacchetto.getCategory(),
                pacchetto.getName(),
                pacchetto.getPrice(),
                pacchetto.getShippingOptions(),
                pacchetto.getStaff(),
                pacchetto.getShippingCost()
        );
        pacchettoAcquistatoRepository.save(pa);
    }

    // Eventualmente metodi per gestire liste di acquisto
    public void acquistaProdotti(List<Prodotto> prodotti) {
        prodotti.forEach(this::acquistaProdotto);
    }

    public void acquistaPacchetti(List<Pacchetto> pacchetti) {
        pacchetti.forEach(this::acquistaPacchetto);
    }
}
