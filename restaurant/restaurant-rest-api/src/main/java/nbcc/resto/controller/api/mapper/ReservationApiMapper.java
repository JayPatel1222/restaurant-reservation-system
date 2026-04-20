package nbcc.resto.controller.api.mapper;

import nbcc.resto.controller.api.dto.RequestReservation;
import nbcc.resto.controller.api.dto.ResponseReservation;
import nbcc.resto.dto.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationApiMapper {

    public ReservationApiMapper() {    }

    public Reservation toDTO(RequestReservation reservation) {

        if (reservation == null) {
            return null;
        }

        return new Reservation()
                .setFirstName(reservation.getFirstName())
                .setLastName(reservation.getLastName())
                .setEmail(reservation.getEmail())
                .setGroupSize(reservation.getGroupSize());
    }

    public ResponseReservation toResponse(Reservation reservation) {

        if (reservation == null) {
            return null;
        }

        return new ResponseReservation()
                .setEventId(reservation.getSeating().getPlatterEventId())
                .setSeatingId(reservation.getSeating().getId())
                .setReservationId(reservation.getReservationId())
                .setFirstName(reservation.getFirstName())
                .setLastName(reservation.getLastName())
                .setEmail(reservation.getEmail())
                .setGroupSize(reservation.getGroupSize())
                .setStatus(reservation.getStatus());
    }
}
