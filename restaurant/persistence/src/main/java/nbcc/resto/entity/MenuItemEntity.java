package nbcc.resto.entity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "menuItem")
@EntityListeners(AuditingEntityListener.class)
public class MenuItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_id")
    private Long menuId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Version
    private Long version;

    public MenuItemEntity() {
    }

    public Long getId() {
        return id;
    }

    public MenuItemEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuItemEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MenuItemEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public MenuItemEntity setVersion(Long version) {
        this.version = version;
        return this;
    }

    public MenuItemEntity setMenuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public Long getMenuId() {
        return menuId;
    }
}
