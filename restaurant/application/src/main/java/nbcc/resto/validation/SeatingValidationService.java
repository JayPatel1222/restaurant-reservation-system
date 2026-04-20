package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Seating;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.repository.UrbanPlatterEventRepository;
import nbcc.resto.service.SeatingService;
import nbcc.resto.service.UrbanPlatterEventService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SeatingValidationService {
    private final AnnotationValidationService validationService;
    private final SeatingRepository seatingRepository;
    private final UrbanPlatterEventRepository eventRepository;

    public SeatingValidationService(AnnotationValidationService validationService, SeatingRepository seatingRepository, UrbanPlatterEventRepository eventRepository) {
        this.validationService = validationService;
        this.seatingRepository = seatingRepository;
        this.eventRepository = eventRepository;
    }

    public Collection<ValidationError> validate(Seating seating) {

        var errors = new ArrayList<ValidationError>();

        errors.addAll(validationService.validate(seating));
        errors.addAll(validateStartDate(seating));
        errors.addAll(validateDuration(seating));
        errors.addAll(validateUniqueName(seating));

        return errors;
    }

    public Collection<ValidationError> validateUniqueName(Seating seating) {

        if (seating.getName() == null || seating.getName().isBlank() || seating.getName().isEmpty())
            return List.of();


        if (seating.getId() != null && seating.getId() > 0) {
            var optionalSeating = seatingRepository.get(seating.getId());

            if (optionalSeating.isPresent()) {
                var dbSeating = optionalSeating.get();

                if (!seating.getName().equalsIgnoreCase(dbSeating.getName())) {
                    if (seatingRepository.exists(seating.getName())) {
                        return List.of(new ValidationError("Seating with that name already exists.", "name", seating.getName()));
                    }
                }
            }
        } else if (seatingRepository.exists(seating.getName())) {
            return List.of(new ValidationError("Seating with that name already exists.", "name", seating.getName()));
        }

        return List.of();
    }

    public Collection<ValidationError> validateStartDate(Seating seating) {

        if (seating.getStartDate() == null)
            return List.of();

        if (seating.getPlatterEvent().getId() == null)
            return List.of();

        var optionalEvent = eventRepository.get(seating.getPlatterEvent().getId());

        if (optionalEvent.isEmpty())
            return List.of();

        var eventDb = optionalEvent.get();

        LocalDateTime eventStartDate = eventDb.getStartDate();
        LocalDateTime eventEndDate = eventDb.getEndDate();
        LocalDateTime seatingEndDate = seating.getStartDate().plusMinutes(seating.getDuration());

        if (seating.getStartDate().isBefore(eventStartDate) || seatingEndDate.isAfter(eventEndDate)) {
            return List.of(new ValidationError("Seating must happen during the Event period.", "startDate", seating.getStartDate()));

        }
        return List.of();
    }

     public Collection<ValidationError> validateDuration(Seating seating) {

        if (seating.getDuration() == null)
            return List.of();

         if (seating.getPlatterEvent().getId() == null)
             return List.of(new ValidationError("Event is required.", "platterEvent.id"));

         var optionalEvent = eventRepository.get(seating.getPlatterEvent().getId());

         if (optionalEvent.isEmpty())
             return List.of();

         var eventDb = optionalEvent.get();

         Integer eventDuration = eventDb.getMinutes();

        if (seating.getDuration() > eventDuration) {
            return List.of(new ValidationError("The Seating Duration must be shorter or equal as the Event Duration.", "duration", seating.getDuration()));
        }
        return List.of();
    }


}
