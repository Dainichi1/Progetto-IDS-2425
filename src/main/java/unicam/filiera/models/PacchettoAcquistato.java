package unicam.filiera.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pacchetti_acquistati")
public class PacchettoAcquistato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column
    private String shippingOptions;

    @Column
    private String staff;

    @Column(name = "shipping_cost")
    private String shippingCost;

    // Altri campi specifici ai pacchetti, se necessario

    // Costruttori, getter e setter
    public PacchettoAcquistato() {}

    public PacchettoAcquistato(String category, String name, BigDecimal price,
                               String shippingOptions, String staff, String shippingCost) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.shippingOptions = shippingOptions;
        this.staff = staff;
        this.shippingCost = shippingCost;
    }

    public Long getId() { return id; }
    public String getCategory() { return category; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public String getShippingOptions() { return shippingOptions; }
    public String getStaff() { return staff; }
    public String getShippingCost() { return shippingCost; }

    public void setId(Long id) { this.id = id; }
    public void setCategory(String category) { this.category = category; }
    public void setName(String name) { this.name = name; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setShippingOptions(String shippingOptions) { this.shippingOptions = shippingOptions; }
    public void setStaff(String staff) { this.staff = staff; }
    public void setShippingCost(String shippingCost) { this.shippingCost = shippingCost; }
}
