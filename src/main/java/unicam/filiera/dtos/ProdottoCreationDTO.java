package unicam.filiera.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class ProdottoCreationDTO {

    @NotBlank(message = "Il nome del prodotto è obbligatorio.")
    private String name;

    @NotNull(message = "Il prezzo è obbligatorio.")
    @Min(value = 0, message = "Il prezzo non può essere negativo.")
    private Double price;

    @NotBlank(message = "La categoria è obbligatoria.")
    private String category;

    @NotBlank(message = "Il campo info è obbligatorio.")
    private String info;

    @NotNull(message = "La quantità disponibile è obbligatoria.")
    @Min(value = 1, message = "La quantità deve essere almeno 1.")
    private Integer availability;

    // Rimuovi le annotazioni di validazione per i campi file
    private MultipartFile images;
    private MultipartFile certificato;

    // Getters e Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getAvailability() {
        return availability;
    }
    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public MultipartFile getImages() {
        return images;
    }
    public void setImages(MultipartFile images) {
        this.images = images;
    }

    public MultipartFile getCertificato() {
        return certificato;
    }
    public void setCertificato(MultipartFile certificato) {
        this.certificato = certificato;
    }
}
