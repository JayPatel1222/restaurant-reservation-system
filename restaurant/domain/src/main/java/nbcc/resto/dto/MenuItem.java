package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;

public class MenuItem {

    private Long id;

    private Long menuId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    private Long version;

    public MenuItem(){

    }

    public Long getId() {
        return id;
    }

    public MenuItem setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MenuItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public MenuItem setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Long getMenuId() {
        return menuId;
    }

    public MenuItem setMenuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }
}
