package nbcc.resto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "urbanPlatterEvent")
@EntityListeners(AuditingEntityListener.class)
public class UrbanPlatterEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id",nullable = true,foreignKey = @ForeignKey(name = "fk_event_menu"))
    private MenuEntity menu;

    @Column(nullable = false,unique = true)
    private String name;

    private String description;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(nullable = false)
    private LocalDateTime startDate;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(nullable = false)
    private LocalDateTime endDate;


    @PositiveOrZero
    @Column(nullable = false)
    private Integer minutes;


    @PositiveOrZero
    @Column(nullable = false)
    private Double price;

    private boolean active;

    private boolean archived;

    @Version
    private Long version;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public UrbanPlatterEventEntity(){

    }

    public UrbanPlatterEventEntity(Long id, String name, String description, LocalDateTime startDate, LocalDateTime endDate, Integer minutes, Double price, boolean active) {
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

    public UrbanPlatterEventEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UrbanPlatterEventEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UrbanPlatterEventEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public UrbanPlatterEventEntity setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public UrbanPlatterEventEntity setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public UrbanPlatterEventEntity setMinutes(Integer minutes) {
        this.minutes = minutes;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public UrbanPlatterEventEntity setPrice(Double price) {
        this.price = price;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public UrbanPlatterEventEntity setActive(boolean active) {
        this.active = active;
        return this;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public UrbanPlatterEventEntity setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UrbanPlatterEventEntity setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public boolean isArchived() {
        return archived;
    }

    public UrbanPlatterEventEntity setArchived(boolean archived) {
        this.archived = archived;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public UrbanPlatterEventEntity setVersion(Long version) {
        this.version = version;
        return this;
    }

    public MenuEntity getMenu() {
        return menu;
    }

    public UrbanPlatterEventEntity setMenu(MenuEntity menu) {
        this.menu = menu;
        return this;
    }
}
