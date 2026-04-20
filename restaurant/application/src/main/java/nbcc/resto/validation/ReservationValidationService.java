package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Reservation;
import nbcc.resto.repository.ReservationRepository;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.service.ReservationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ReservationValidationService {

    private final AnnotationValidationService validationService;
    private final ReservationRepository reservationRepository;
    private final SeatingRepository seatingRepository;

    public ReservationValidationService(AnnotationValidationService validationService, ReservationRepository reservationRepository, SeatingRepository seatingRepository) {
        this.validationService = validationService;
        this.reservationRepository = reservationRepository;
        this.seatingRepository = seatingRepository;
    }

    public Collection<ValidationError> validate(Reservation reservation) {

        var errors = new ArrayList<ValidationError>();
        errors.addAll(validationService.validate(reservation));
        errors.addAll(validateTableCapacity(reservation));
        errors.addAll(validateTableAvailable(reservation));
        errors.addAll(validateSeatingBelongsToEvent(reservation));

       return errors;
    }

    public Collection<ValidationError> validateTableAvailable(Reservation reservation) {

        if (reservation.getSeating() == null || reservation.getAssignedTable() == null )
            return List.of();

       var approved =reservationRepository.getApproved(reservation.getSeating().getId());
       for (Reservation r : approved) {
           if (r.getAssignedTable() != null && r.getAssignedTable().getId().equals(reservation.getAssignedTable().getId())) {
               return List.of(new ValidationError("Table assigned to another Reservation."));
           }
       }

         return List.of();
    }

    public Collection<ValidationError> validateTableCapacity(Reservation reservation) {

        if (reservation.getSeating() == null || reservation.getAssignedTable() == null )
            return List.of();

        var selectedTable = reservation.getAssignedTable();

        if (selectedTable.getCapacity() < reservation.getGroupSize())
            return List.of(new ValidationError(" Table capacity not enough for this group size."));

         return List.of();
    }

    public Collection<ValidationError> validateSeatingBelongsToEvent(Reservation reservation) {

        if (reservation.getSeating() == null )
            return List.of();

        var seating = reservation.getSeating();
        var optionalSeating = seatingRepository.get(seating.getId());

        if (optionalSeating.isEmpty()) {
            return List.of(new ValidationError("Seating does  not exist.", "seating", seating));
        }

        var platterEvent = reservation.getSeating().getPlatterEvent();

        var dbSeatingEvent = optionalSeating.get().getPlatterEvent();

        if (platterEvent != null && !dbSeatingEvent.getId().equals(platterEvent.getId())) {
            return List.of(new ValidationError("Seating not found on selected Event.", "seating", seating));
        }

        return List.of();
    }


}
