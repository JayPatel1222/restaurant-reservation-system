package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.Seating;

import java.util.List;
import java.util.Optional;

public interface SeatingRepository {

    List<Seating> getAll();
    List<Seating> getAll(Long eventId);
    Optional<Seating> get(Long id);
    Seating create(Seating seating);
    boolean exists(String name);
    void delete(Long id);
    Seating update(Seating seating) throws ConcurrencyException;
    boolean existReservationForSeating(Long seatingId);
}
