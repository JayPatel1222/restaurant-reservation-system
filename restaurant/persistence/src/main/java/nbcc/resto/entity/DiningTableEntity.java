package nbcc.resto.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "dining_table")
@EntityListeners(AuditingEntityListener.class)
public class DiningTableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Positive
    private Integer capacity;

    @Version
    private Long version;

    private Boolean archived;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public DiningTableEntity() {
    }

    public Long getId() {
        return id;
    }

    public DiningTableEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DiningTableEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public DiningTableEntity setCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public DiningTableEntity setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Boolean getArchived() {
        return archived;
    }

    public DiningTableEntity setArchived(Boolean archived) {
        this.archived = archived;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public DiningTableEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public DiningTableEntity setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }


}
