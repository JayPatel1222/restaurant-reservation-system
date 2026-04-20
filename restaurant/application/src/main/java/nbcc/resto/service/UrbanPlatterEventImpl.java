package nbcc.resto.service;

import nbcc.common.exception.ConcurrencyException;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.EventDetails;
import nbcc.resto.dto.Seating;
import nbcc.resto.dto.SeatingDetails;
import nbcc.resto.dto.UrbanPlatterEvent;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.repository.UrbanPlatterEventRepository;
import nbcc.resto.validation.EventValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UrbanPlatterEventImpl implements UrbanPlatterEventService {

    private final Logger logger = LoggerFactory.getLogger(UrbanPlatterEventImpl.class);
    private final UrbanPlatterEventRepository eventRepository;
    private final SeatingRepository seatingRepository;
    private final EventValidationService validationService;

    public UrbanPlatterEventImpl(UrbanPlatterEventRepository eventRepository, SeatingRepository seatingRepository, EventValidationService validationService) {
        this.eventRepository = eventRepository;
        this.seatingRepository = seatingRepository;
        this.validationService = validationService;
    }

    @Override
    public Result<Collection<UrbanPlatterEvent>> getAll() {
        try{
            var results = eventRepository.getAll();
            Collection<UrbanPlatterEvent> unArchivedEvents = results.stream().filter(event -> !event.isArchived()).toList();
            return ValidationResults.success(unArchivedEvents);
        } catch (Exception e){
            logger.error("Error retrieving all Events", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<UrbanPlatterEvent> get(Long id) {
        try{
            return ValidationResults.success(eventRepository.get(id));
        } catch (Exception e){
            logger.error("Error retrieving all Event with id {}", id,e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<UrbanPlatterEvent> create(UrbanPlatterEvent event) {
        try{
            var errors = validationService.validate(event);

            if(errors.isEmpty()){
                return ValidationResults.success(eventRepository.create(event));
            }

            logger.debug("Validation errors for event create {}: {}",event,errors);
            return ValidationResults.invalid(event,errors);
        } catch (Exception e){
            logger.error("Error creating event {}",event,e);
            return ValidationResults.error(event);
        }
    }

    @Override
    public ValidatedResult<UrbanPlatterEvent> update(UrbanPlatterEvent event) {
        try{
            var errors = validationService.validate(event);

            if(errors.isEmpty()){
                try {
                    return ValidationResults.success(eventRepository.update(event));
                } catch (ConcurrencyException e){
                    errors.add(new ValidationError("Event was modified since it was displayed, Please refresh and try again"));
                }
            }

            logger.debug("Validation errors for event update {}: {}",event,errors);
            return ValidationResults.invalid(event,errors);
        } catch (Exception e){
            logger.error("Error updating event {}",event,e);
            return ValidationResults.error(event);
        }
    }

    @Override
    public ValidatedResult<Void> delete(Long id) {
        try {
            var event = eventRepository.get(id);
            if (event.isPresent()) {
                var needsToBeArchive = validationService.checkIfEventNeedsToBeArchive(event.get());
                if (needsToBeArchive) {
                    event.get().setArchived(true);
                    if(event.get().isActive()){
                        event.get().setActive(false);
                    }
                    logger.debug("Event with id {} is now Archived",id);
                    update(event.get());
                    return ValidationResults.success();
                }
            }
            eventRepository.delete(id);
            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Error deleting the event with id {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<UrbanPlatterEvent>> search(String name, LocalDateTime startDate, LocalDateTime endDate) {
        try{
            var result = eventRepository.search(name,startDate,endDate);
            return ValidationResults.success(result);
        } catch (Exception e) {
            logger.error("Error find events", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<UrbanPlatterEvent>> filter(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            var result = eventRepository.filter(startDate,endDate);
            return ValidationResults.success(result);
        } catch (Exception e) {
            logger.error("Error filtering events", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<EventDetails>> getAllEventDetails() {
        try {
            var events = eventRepository.getAll();

            Collection<EventDetails> details = new ArrayList<>();
            
            events.forEach(event -> {
                var eventDetails = new EventDetails();
                var seating = seatingRepository.getAll(event.getId());
                if (seating != null) {
                    eventDetails.setSeatings(assignedSeatingDetails(seating));
                }
                eventDetails.setEvent(event);
                details.add(eventDetails);
            });
            return ValidationResults.success(details);
        } catch (Exception e) {
            logger.error("Error getting event details with seatings");
            return ValidationResults.error(e);
        }
    }
    @Override
    public ValidatedResult<EventDetails> getDetailByEventId(Long id){
        try{
            var seating = seatingRepository.getAll(id);
            var eventDetails = new EventDetails();
            var event = eventRepository.get(id);

            eventDetails.setEvent(event.orElse(null));

            if (seating != null) {
                eventDetails.setSeatings(assignedSeatingDetails(seating));
            }

            return ValidationResults.success(eventDetails);
        } catch (Exception e){
            logger.error("Error retrieving event and details");
            return ValidationResults.error(e);
        }
    }

    private List<SeatingDetails> assignedSeatingDetails(List<Seating> seating){
        return seating.stream()
                .map(s -> new SeatingDetails()
                        .setName(s.getName())
                        .setDuration(s.getDuration())
                        .setId(s.getId())
                        .setVersion(s.getVersion())
                        .setStartDate(s.getStartDate())
                        .setArchived(s.getArchived())
                )
                .toList();
    }
}
