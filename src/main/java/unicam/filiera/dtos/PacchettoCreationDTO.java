package unicam.filiera.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PacchettoCreationDTO {

    @NotBlank(message = "Il nome del pacchetto è obbligatorio.")
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

    private List<MultipartFile> images;

    private List<MultipartFile> certificati;

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

    public List<MultipartFile> getImages() {
        return images;
    }
    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    public List<MultipartFile> getCertificati() {
        return certificati;
    }
    public void setCertificati(List<MultipartFile> certificati) {
        this.certificati = certificati;
    }
}

