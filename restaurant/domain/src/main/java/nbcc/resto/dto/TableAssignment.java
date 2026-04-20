package nbcc.resto.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TableAssignment {

    private Long id;

    @NotNull(message = "A valid seating is required")
    private Seating seating;

    @NotNull(message = "A valid table is required")
    private DiningTable diningTable;

    private LocalDateTime associatedDate;

    public TableAssignment() {
        associatedDate = LocalDateTime.now();
    }

    // check constructor
    public TableAssignment(Long seatingId, Long diningTableId) {
        this();
        this.seating = new Seating().setId(seatingId);
        this.diningTable = new DiningTable().setId(diningTableId);
    }

    public Long getId() {
        return id;
    }

    public TableAssignment setId(Long id) {
        this.id = id;
        return this;
    }

    public Seating getSeating() {
        return seating;
    }

    public TableAssignment setSeating(Seating seating) {
        this.seating = seating;
        return this;
    }

    public DiningTable getDiningTable() {
        return diningTable;
    }

    public TableAssignment setDiningTable(DiningTable diningTable) {
        this.diningTable = diningTable;
        return this;
    }

    public LocalDateTime getAssociatedDate() {
        return associatedDate;
    }

    public TableAssignment setAssociatedDate(LocalDateTime associatedDate) {
        this.associatedDate = associatedDate;
        return this;
    }
}


