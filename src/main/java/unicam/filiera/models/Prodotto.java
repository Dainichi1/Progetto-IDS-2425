package unicam.filiera.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "prodotti")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column
    private String images; // Percorso delle immagini caricate

    @Column
    private String certificato; // Percorso del certificato caricato

    @Column(nullable = false)
    private String stato; // Stato del prodotto: "pendente", "approvato", "rifiutato"

    @Column(columnDefinition = "TEXT")
    private String curatorComments; // Commenti o modifiche del curatore

    // Costruttore no-arg pubblico richiesto da JPA
    public Prodotto() {
    }

    // Costruttore privato per Builder Pattern
    private Prodotto(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.category = builder.category;
        this.availability = builder.availability;
        this.info = builder.info;
        this.images = builder.images;
        this.certificato = builder.certificato;
        this.stato = builder.stato != null ? builder.stato : "pendente"; // Stato predefinito
        this.curatorComments = builder.curatorComments;
    }

    // Getters
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

    public String getCertificato() {
        return certificato;
    }

    public String getStato() {
        return stato;
    }

    public String getCuratorComments() {
        return curatorComments;
    }

    // Setters
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

    public void setCertificato(String certificato) {
        this.certificato = certificato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public void setCuratorComments(String curatorComments) {
        this.curatorComments = curatorComments;
    }

    // Builder Pattern
    public static class Builder {
        private String name;
        private BigDecimal price;
        private String category;
        private int availability;
        private String info;
        private String images;
        private String certificato;
        private String stato;
        private String curatorComments;

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

        public Builder certificato(String certificato) {
            this.certificato = certificato;
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

        public Prodotto build() {
            return new Prodotto(this);
        }
    }
}
