package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.common.result.Result;
import nbcc.resto.dto.EventDetails;
import nbcc.resto.dto.UrbanPlatterEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UrbanPlatterEventRepository {
    List<UrbanPlatterEvent> getAll();
    Optional<UrbanPlatterEvent> get(Long id);
    UrbanPlatterEvent create(UrbanPlatterEvent event);
    UrbanPlatterEvent update(UrbanPlatterEvent event) throws ConcurrencyException;
    void delete(Long id);
    List<UrbanPlatterEvent> search(String name, LocalDateTime startDate, LocalDateTime endDate);
    List<UrbanPlatterEvent> filter(LocalDateTime startDate,LocalDateTime endDate);
    boolean exists(String name);
}