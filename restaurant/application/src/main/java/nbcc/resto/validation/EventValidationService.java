package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.UrbanPlatterEvent;
import nbcc.resto.repository.ReservationRepository;
import nbcc.resto.repository.UrbanPlatterEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class EventValidationService {
    private final AnnotationValidationService annotationValidationService;
    private final UrbanPlatterEventRepository eventRepository;
    private final ReservationRepository reservationRepository;

    public EventValidationService(AnnotationValidationService annotationValidationService, UrbanPlatterEventRepository eventRepository, ReservationRepository reservationRepository) {
        this.annotationValidationService = annotationValidationService;
        this.eventRepository = eventRepository;
        this.reservationRepository = reservationRepository;
    }

    public Collection<ValidationError> validate(UrbanPlatterEvent event) {
        var validationErrors = new ArrayList<ValidationError>();

        validationErrors.addAll(annotationValidationService.validate(event));
        validationErrors.addAll(validateUniqueName(event));
        if(event.getEndDate() != null && event.getStartDate() != null) {
            validationErrors.addAll(validateEventDates(event));
        }

        return validationErrors;
    }

    private Collection<ValidationError> validateUniqueName(UrbanPlatterEvent event){

        if(event.getId() != null) {
            var dbEvent = eventRepository.get(event.getId());

            if(dbEvent.isPresent() && !event.getName().equals(dbEvent.get().getName())){

                var updatedEventNameExists = eventRepository.exists(event.getName());

                if(updatedEventNameExists) {
                    return List.of(new ValidationError("Event with this name already exists, Please select different name", "name"));
                }
            }
            return List.of();
        }
        var eventNameExists = eventRepository.exists(event.getName());

        if (eventNameExists) {
            return List.of(new ValidationError("Event with this name already exists, Please select different name", "name"));
        }
        return List.of();
    }

    public boolean checkIfEventNeedsToBeArchive(UrbanPlatterEvent event){
        var reservation = reservationRepository.getAll(event.getId());
        if(!reservation.isEmpty()){
            return true;
        }
        LocalDateTime currentDateTime  = LocalDateTime.now();
        return event.getEndDate().isBefore(currentDateTime);
    }

    private Collection<ValidationError> validateEventDates(UrbanPlatterEvent event){
        if(event != null){
            if(event.getEndDate().toLocalDate().isBefore(event.getStartDate().toLocalDate())){
                return List.of(new ValidationError("EndDate cannot be before StartDate"));
            }
            if(event.getStartDate().isEqual(event.getEndDate())){
                return List.of(new ValidationError("Start Time and End Time Cannot be same"));
            }
        }
        return List.of();
    }
}
