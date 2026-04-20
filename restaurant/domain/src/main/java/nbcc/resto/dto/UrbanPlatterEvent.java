package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UrbanPlatterEvent {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private Menu menu;

    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Start Date is required")
    private LocalDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "End Date is required")
    private LocalDateTime endDate;

    @PositiveOrZero(message = "Minutes should be greater than 0")
    @NotNull(message = "Minutes is required")
    private Integer minutes;

    @PositiveOrZero(message = "Price should be greater than 0")
    @NotNull(message = "Price is required")
    private Double price;

    private boolean active;

    private boolean archived;

    private LocalDate createdAt;

    private LocalDateTime updatedAt;

    private Long version;

    public UrbanPlatterEvent(){

    }

    public UrbanPlatterEvent(Long id, String name, String description, LocalDateTime startDate, LocalDateTime endDate, Integer minutes, Double price, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minutes = minutes;
        this.price = price;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public UrbanPlatterEvent setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UrbanPlatterEvent setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UrbanPlatterEvent setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public UrbanPlatterEvent setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public UrbanPlatterEvent setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public UrbanPlatterEvent setMinutes(Integer minutes) {
        this.minutes = minutes;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public UrbanPlatterEvent setPrice(Double price) {
        this.price = price;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public UrbanPlatterEvent setActive(boolean active) {
        this.active = active;
        return this;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public UrbanPlatterEvent setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UrbanPlatterEvent setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public boolean isArchived() {
        return archived;
    }

    public UrbanPlatterEvent setArchived(boolean archived) {
        this.archived = archived;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public UrbanPlatterEvent setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Menu getMenu() {
        return menu;
    }

    public UrbanPlatterEvent setMenu(Menu menu) {
        this.menu = menu;
        return this;
    }
}
