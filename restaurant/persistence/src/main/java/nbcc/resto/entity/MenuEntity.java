package nbcc.resto.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu")
@EntityListeners(AuditingEntityListener.class)
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuItemEntity> items;

    @Version
    private Long version;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public MenuEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public MenuEntity() {
    }

    public Long getId() {
        return id;
    }

    public MenuEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MenuEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public MenuEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public MenuEntity setVersion(Long version) {
        this.version = version;
        return this;
    }

    public List<MenuItemEntity> getItems() {
        return items;
    }

    public MenuEntity setItems(List<MenuItemEntity> items) {
        this.items = items;
        return this;
    }
}
