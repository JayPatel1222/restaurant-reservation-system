package nbcc.resto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class Reservation {

    private Long id;
    private String reservationId;

    @NotNull
    private Seating seating;

    private DiningTable assignedTable;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Email Address is required")
    @Email
    private String email;

    @NotNull(message = "Group size is required")
    @Positive(message = "Group size must be greater than 0")
    private Integer groupSize;

    private ReservationStatus status;

    private Long version;

    public Reservation() {}

    public Reservation(Seating seating) { this.seating = seating; }

    public Reservation(Reservation reservation) {
        this(reservation.getId(), reservation.getReservationId(), reservation.getSeating(),
        reservation.getFirstName(), reservation.getLastName(), reservation.getEmail(),
        reservation.getGroupSize(), reservation.getStatus(), reservation.getVersion(), reservation.getAssignedTable());
    }

    public Reservation(Long id, String reservationId, Seating seating, String firstName, String lastName, String email, Integer groupSize, ReservationStatus status, Long version,  DiningTable assignedTable) {
        this.id = id;
        this.reservationId = reservationId;
        this.seating = seating;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.groupSize = groupSize;
        this.status = status;
        this.version = version;
        this.assignedTable = assignedTable;
    }

    public Long getId() {
        return id;
    }

    public Reservation setId(Long id) {
        this.id = id;
        return this;
    }

    public String getReservationId() {
        return reservationId;
    }

    public Reservation setReservationId(String reservationId) {
        this.reservationId = reservationId;
        return this;
    }

    public Seating getSeating() {
        return seating;
    }

    public Reservation setSeating(Seating seating) {
        this.seating = seating;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Reservation setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Reservation setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Reservation setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getGroupSize() {
        return groupSize;
    }

    public Reservation setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
        return this;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public Reservation setStatus(ReservationStatus status) {
        this.status = status;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public Reservation setVersion(Long version) {
        this.version = version;
        return this;
    }

    public DiningTable getAssignedTable() {
        return assignedTable;
    }

    public Reservation setAssignedTable(DiningTable assignedTable) {
        this.assignedTable = assignedTable;
        return this;
    }
}
