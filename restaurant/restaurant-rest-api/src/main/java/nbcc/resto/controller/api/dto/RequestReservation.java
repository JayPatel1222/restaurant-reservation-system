package nbcc.resto.controller.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RequestReservation {

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

    public RequestReservation() {}

    public Integer getGroupSize() {
        return groupSize;
    }

    public RequestReservation setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public RequestReservation setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public RequestReservation setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public RequestReservation setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Long getSeatingId() {
        return seatingId;
    }

    public RequestReservation setSeatingId(Long seatingId) {
        this.seatingId = seatingId;
        return this;
    }

    public Long getEventId() {
        return eventId;
    }

    public RequestReservation setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }
}
