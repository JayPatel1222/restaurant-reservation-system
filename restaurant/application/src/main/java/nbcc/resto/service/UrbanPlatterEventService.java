package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.EventDetails;
import nbcc.resto.dto.UrbanPlatterEvent;
import java.time.LocalDateTime;
import java.util.Collection;

public interface UrbanPlatterEventService {
    Result<Collection<UrbanPlatterEvent>> getAll();
    ValidatedResult<UrbanPlatterEvent> get(Long id);
    ValidatedResult<UrbanPlatterEvent> create(UrbanPlatterEvent event);
    ValidatedResult<UrbanPlatterEvent> update(UrbanPlatterEvent event);
    ValidatedResult<Void> delete(Long id);
    Result<Collection<UrbanPlatterEvent>>search(String name, LocalDateTime startDate, LocalDateTime endDate);
    Result<Collection<UrbanPlatterEvent>>filter(LocalDateTime startDate, LocalDateTime endDate);
    Result<Collection<EventDetails>> getAllEventDetails();
    ValidatedResult<EventDetails> getDetailByEventId(Long id);

}
