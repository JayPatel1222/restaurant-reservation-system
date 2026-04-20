package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.ReservationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {
    Reservation create(Reservation reservation);
    List<Reservation> getAll();
    List<Reservation> getAll(Long eventId);
    List<Reservation> filter(Long eventId, ReservationStatus status);
    Optional<Reservation> get(Long id);
    Optional<Reservation> getByUUID(String reservationId);
    List<Reservation> getApproved( Long seatingId);
    Reservation update(Reservation reservation) throws ConcurrencyException;

}
