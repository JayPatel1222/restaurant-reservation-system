package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.DiningTable;

import java.util.List;
import java.util.Optional;

public interface DiningTableRepository {
    List<DiningTable> getAll();
    Optional<DiningTable> get(Long id);
    DiningTable create(DiningTable diningTable);
    DiningTable update(DiningTable diningTable) throws ConcurrencyException;
    void delete(Long id);
    boolean exists(String name);
    boolean existReservationForTable(Long id);
    boolean existsSeatingForTable(Long id);
}
