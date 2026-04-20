package nbcc.resto.entity;


import jakarta.persistence.*;
import nbcc.resto.dto.ReservationStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservation")
@EntityListeners(AuditingEntityListener.class)
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reservationId; // modify

    @ManyToOne
    @JoinColumn(name = "seating_id", foreignKey = @ForeignKey(name = "fk_reservation_seating"))
    private SeatingEntity seating;

    @ManyToOne
    @JoinColumn(name = "table_id", foreignKey = @ForeignKey(name = "fk_reservation_diningtable"))
    private DiningTableEntity assignedTable;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer groupSize;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Version
    private Long version;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public ReservationEntity() {
    }

    public Long getId() {
        return id;
    }

    public ReservationEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public SeatingEntity getSeating() {
        return seating;
    }

    public ReservationEntity setSeating(SeatingEntity seating) {
        this.seating = seating;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ReservationEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public ReservationEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ReservationEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getGroupSize() {
        return groupSize;
    }

    public ReservationEntity setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
        return this;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public ReservationEntity setStatus(ReservationStatus status) {
        this.status = status;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public ReservationEntity setVersion(Long version) {
        this.version = version;
        return this;
    }

    public String getReservationId() {
        return reservationId;
    }

    public ReservationEntity setReservationId(String reservationId) {
        this.reservationId = reservationId;
        return this;
    }

    public DiningTableEntity getAssignedTable() {
        return assignedTable;
    }

    public ReservationEntity setAssignedTable(DiningTableEntity assignedTable) {
        this.assignedTable = assignedTable;
        return this;
    }
}
