package nbcc.resto.service;

import nbcc.common.exception.ConcurrencyException;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Seating;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.validation.SeatingValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SeatingServiceImpl implements SeatingService {

    private final Logger logger = LoggerFactory.getLogger(SeatingServiceImpl.class);

    private final SeatingRepository seatingRepository;
    private final SeatingValidationService validationService;


    public SeatingServiceImpl(SeatingRepository seatingRepository, SeatingValidationService validationService) {
        this.seatingRepository = seatingRepository;
        this.validationService = validationService;
    }

    @Override
    public Result<Collection<Seating>> getAll() {
        try {
            return ValidationResults.success(seatingRepository.getAll());
        } catch (Exception e) {
            logger.error("Error retrieving all seating", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Collection<Seating>> getAll(Long eventId) {

        try {
            return ValidationResults.success(seatingRepository.getAll(eventId));
        } catch (Exception e) {
            logger.error("Error retrieving all seating", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Seating> get(Long id) {
        try {
            return ValidationResults.success(seatingRepository.get(id));
        } catch (Exception e) {
            logger.error("Error retrieving seating with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Seating> create(Seating seating) {
        try {
            var errors = validationService.validate(seating);

            if (errors.isEmpty()) {
                seating.setArchived(false);
                return ValidationResults.success(seatingRepository.create(seating));
            } else {
                logger.debug("Validation errors for seating create {}: {}", seating, errors);
                return ValidationResults.invalid(seating, errors);
            }
        } catch (Exception e) {
            logger.error("Error while creating seating {}", seating, e);
            return ValidationResults.error(seating);
        }
    }

    @Override
    public ValidatedResult<Seating> update(Seating seating) {
        try {
            var errors = validationService.validate(seating);

            if (errors.isEmpty()) {
                try {
                    seating.setArchived(false);
                    return ValidationResults.success(seatingRepository.update(seating));
                } catch(ConcurrencyException e) {
                    errors.add(new ValidationError("This seating was modified since it was displayed, please refresh and try again"));
                }
            }
            logger.debug("Validation errors found for Seating update {}: {}", seating, errors);
            return ValidationResults.invalid(seating, errors);

        } catch (Exception e) {
            logger.error("Error updating seating {}",seating, e);
            return ValidationResults.error(seating);
        }
    }

    @Override
    public ValidatedResult<Seating> delete(Long id) {
        try {
            var seatingOptional =  this.seatingRepository.get(id);

            if (seatingOptional.isEmpty()) {
                logger.debug("Seating with id {} not found", id);
                return ValidationResults.invalid( new Seating(), "Seating not found." );
            }

            var seating = seatingOptional.get();
            boolean hasReservation = seatingRepository.existReservationForSeating(seating.getId());

            if (hasReservation) {
                seating.setArchived(true);
                seatingRepository.update(seating);
                logger.debug("Seating with id {} has been archived", id);
            } else {
                seatingRepository.delete(id);
                logger.debug("Seating with id {} has been deleted", id);
            }

            return ValidationResults.success();
        } catch(Exception e) {
            logger.error("Error deleting seating with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }
}
