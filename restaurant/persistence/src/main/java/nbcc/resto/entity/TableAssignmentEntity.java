package nbcc.resto.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.dto.Seating;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "table_assignment")
@EntityListeners(AuditingEntityListener.class)
public class TableAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seating_id", foreignKey = @ForeignKey(name = "fk_seating_assignment"), nullable = false)
    private SeatingEntity seating;

    @ManyToOne
    @JoinColumn(name = "table_id", foreignKey = @ForeignKey(name = "fk_table_assignment"), nullable = false)
    private DiningTableEntity diningTable;

    private LocalDateTime associatedDate;

    public TableAssignmentEntity() {    }

    public Long getId() {
        return id;
    }

    public TableAssignmentEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public SeatingEntity getSeating() {
        return seating;
    }

    public TableAssignmentEntity setSeating(SeatingEntity seating) {
        this.seating = seating;
        return this;
    }

    public DiningTableEntity getDiningTable() {
        return diningTable;
    }

    public TableAssignmentEntity setDiningTable(DiningTableEntity diningTable) {
        this.diningTable = diningTable;
        return this;
    }

    public LocalDateTime getAssociatedDate() {
        return associatedDate;
    }

    public TableAssignmentEntity setAssociatedDate(LocalDateTime associatedDate) {
        this.associatedDate = associatedDate;
        return this;
    }
}
