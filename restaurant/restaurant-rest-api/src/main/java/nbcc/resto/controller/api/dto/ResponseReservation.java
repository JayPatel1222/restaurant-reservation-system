package nbcc.resto.controller.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import nbcc.resto.dto.ReservationStatus;
import org.apache.coyote.Response;

public class ResponseReservation {

    private String reservationId;

    @NotNull
    private Long eventId;

    @NotNull
    private Long seatingId;

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

    public ResponseReservation() {
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public ResponseReservation setStatus(ReservationStatus status) {
        this.status = status;
        return this;
    }

    public Integer getGroupSize() {
        return groupSize;
    }

    public ResponseReservation setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ResponseReservation setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ResponseReservation setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public ResponseReservation setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Long getSeatingId() {
        return seatingId;
    }

    public ResponseReservation setSeatingId(Long seatingId) {
        this.seatingId = seatingId;
        return this;
    }

    public Long getEventId() {
        return eventId;
    }

    public ResponseReservation setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public String getReservationId() {
        return reservationId;
    }

    public ResponseReservation setReservationId(String reservationId) {
        this.reservationId = reservationId;
        return this;
    }
}
