package unicam.filiera.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Rappresenta un "pacchetto" creato, ad esempio, dal distributore.
 * Le immagini (images) sono file immagine,
 * mentre i certificati (certificati) sono file di testo.
 */
@Entity
@Table(name = "pacchetti")
public class Pacchetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Per coerenza con Prodotto, possiamo usare i nomi "nome", "prezzo", ecc.
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int availability;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String info;

    @Column(columnDefinition = "TEXT")
    private String images; // Percorsi delle immagini (file immagine), salvate come CSV (opzionale)

    @Column(columnDefinition = "TEXT")
    private String certificati; // Percorsi dei certificati (file di testo), salvati come CSV (opzionale)

    @Column(nullable = false)
    private String stato; // "pendente", "approvato", "rifiutato", "pubblicato"...

    @Column(columnDefinition = "TEXT")
    private String curatorComments; // commenti del curatore

    @Column
    private String staff; // Esempio: "DISTRIBUTORE"

    @Column
    private String shippingOptions;

    // Costruttore no-arg richiesto da JPA
    public Pacchetto() {
    }

    // Costruttore privato per il Builder
    private Pacchetto(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.category = builder.category;
        this.availability = builder.availability;
        this.info = builder.info;
        this.images = builder.images;
        this.certificati = builder.certificati;
        this.stato = (builder.stato != null ? builder.stato : "pendente");
        this.curatorComments = builder.curatorComments;
        this.staff = builder.staff;
        this.shippingOptions = builder.shippingOptions;
    }

    /* ====================== GETTER ====================== */
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public int getAvailability() {
        return availability;
    }

    public String getInfo() {
        return info;
    }

    public String getImages() {
        return images;
    }

    public String getCertificati() {
        return certificati;
    }

    public String getStato() {
        return stato;
    }

    public String getCuratorComments() {
        return curatorComments;
    }

    public String getStaff() {
        return staff;
    }

    public String getShippingOptions() {
        return shippingOptions;
    }

    /* ====================== SETTER ====================== */
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setCertificati(String certificati) {
        this.certificati = certificati;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public void setCuratorComments(String curatorComments) {
        this.curatorComments = curatorComments;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public void setShippingOptions(String shippingOptions) {
        this.shippingOptions = shippingOptions;
    }

    /* ====================== BUILDER ====================== */
    public static class Builder {
        private String name;
        private BigDecimal price;
        private String category;
        private int availability;
        private String info;
        private String images;
        private String certificati;
        private String stato;
        private String curatorComments;
        private String staff;
        private String shippingOptions;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder availability(int availability) {
            this.availability = availability;
            return this;
        }

        public Builder info(String info) {
            this.info = info;
            return this;
        }

        public Builder images(String images) {
            this.images = images;
            return this;
        }

        public Builder certificati(String certificati) {
            this.certificati = certificati;
            return this;
        }

        public Builder stato(String stato) {
            this.stato = stato;
            return this;
        }

        public Builder curatorComments(String curatorComments) {
            this.curatorComments = curatorComments;
            return this;
        }

        public Builder staff(String staff) {
            this.staff = staff;
            return this;
        }

        public Builder shippingOptions(String shippingOptions) {
            this.shippingOptions = shippingOptions;
            return this;
        }

        public Pacchetto build() {
            return new Pacchetto(this);
        }
    }
}
