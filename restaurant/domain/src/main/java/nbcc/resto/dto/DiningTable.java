package nbcc.resto.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

public class DiningTable {

    private Long id;

    private String name;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be greater than 0")
    private Integer capacity;

    private Long version;

    private Boolean archived;

    public DiningTable() {    }

    public DiningTable(DiningTable diningTable) {
        this(diningTable.getId(), diningTable.getCapacity(), diningTable.getName(), diningTable.getVersion(), diningTable.getArchived());
    }

    public DiningTable(Long id, Integer capacity, String name, Long version, Boolean archived) {
        this.id = id;
        this.capacity = capacity;
        this.name = name;
        this.version = version;
        this.archived = archived;
    }

    public Long getId() {
        return id;
    }

    public DiningTable setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DiningTable setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public DiningTable setCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public DiningTable setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Boolean getArchived() {
        return archived;
    }

    public DiningTable setArchived(Boolean archived) {
        this.archived = archived;
        return this;
    }
}
