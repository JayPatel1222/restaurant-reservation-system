package nbcc.resto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "seating")
@EntityListeners(AuditingEntityListener.class)
public class SeatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", foreignKey = @ForeignKey(name = "fk_seating_event"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UrbanPlatterEventEntity platterEvent;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Positive
    @Column(nullable = false)
    private Integer duration;

    @Version
    private Long version;

    private Boolean archived;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public SeatingEntity() {
    }

    public UrbanPlatterEventEntity getPlatterEvent() {
        return platterEvent;
    }

    public SeatingEntity setPlatterEvent(UrbanPlatterEventEntity platterEvent) {
        this.platterEvent = platterEvent;
        return this;
    }

    public Long getId() {
        return id;
    }

    public SeatingEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SeatingEntity setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public SeatingEntity setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public Integer getDuration() {
        return duration;
    }

    public SeatingEntity setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public SeatingEntity setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Boolean getArchived() {
        return archived;
    }

    public SeatingEntity setArchived(Boolean archived) {
        this.archived = archived;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SeatingEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public SeatingEntity setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
