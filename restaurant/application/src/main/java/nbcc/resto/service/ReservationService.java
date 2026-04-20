package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.dto.UrbanPlatterEvent;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public interface ReservationService {
    ValidatedResult<Reservation> create(Reservation reservation);
    ValidatedResult<Collection<Reservation>> getAll(Long eventId);
    Result<Collection<Reservation>> getAll();
    ValidatedResult<Reservation> get(Long id);
    Result<Collection<Reservation>> filter(Long eventId, ReservationStatus status);
    ValidatedResult<Reservation> getByUUID(String reservationId);
    Result<Collection<Reservation>> getApproved(Long seatingId );
    Result<Collection<DiningTable>> getAvailableTables(Long seatingId);
    ValidatedResult<Reservation> update(Reservation reservation);

}
