package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public class Menu {

    private Long id ;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private List<MenuItem> items;

    private Long version;

    private LocalDateTime createdAt;

    public Menu(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public Menu(){

    }

    public Long getId() {
        return id;
    }

    public Menu setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Menu setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Menu setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Menu setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public Menu setVersion(Long version) {
        this.version = version;
        return this;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public Menu setItems(List<MenuItem> items) {
        this.items = items;
        return this;
    }
}
