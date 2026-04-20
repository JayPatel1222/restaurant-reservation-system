package nbcc.resto.service;

import nbcc.common.exception.ConcurrencyException;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.repository.DiningTableRepository;
import nbcc.resto.validation.DiningTableValidationService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiningTableServiceImpl implements DiningTableService {

    private final Logger logger = LoggerFactory.getLogger(DiningTableServiceImpl.class);

    private final DiningTableRepository diningTableRepository;
    private final DiningTableValidationService validationService;

    public DiningTableServiceImpl(DiningTableRepository diningTableRepository, DiningTableValidationService validationService) {
        this.diningTableRepository = diningTableRepository;
        this.validationService = validationService;
    }

    @Override
    public Result<List<DiningTable>> getAll() {
        try {
            return ValidationResults.success(diningTableRepository.getAll());
        } catch (Exception e){
            logger.error("Error retrieving all dining tables", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<DiningTable> get(Long id) {
        try {
            return ValidationResults.success(diningTableRepository.get(id));
        } catch (Exception e){
            logger.error("Error retrieving dining table with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<DiningTable> create(DiningTable diningTable) {
        try {
            var errors = validationService.validate(diningTable);

            if (errors.isEmpty()) {
                diningTable.setArchived(false);
                diningTable = diningTableRepository.create(diningTable);

                if (diningTable.getName() == null || diningTable.getName().isBlank()) {
                   diningTable.setName("Table " + diningTable.getId());
                   logger.debug("Renaming Dining table to {}", diningTable.getName());
                }

                return ValidationResults.success(diningTableRepository.update(diningTable));
            } else {
                logger.debug("Validation errors found for Dining Table {}: {}", diningTable, errors);
                return ValidationResults.invalid(diningTable, errors);
            }
        } catch (Exception e) {
            logger.error("Error creating dining table {}", diningTable, e);
            return ValidationResults.error(diningTable);
        }
    }

    @Override
    public ValidatedResult<DiningTable> update(DiningTable diningTable) {
        try {
            var errors = validationService.validate(diningTable);

            if (errors.isEmpty()) {
                try {
                    if (diningTable.getName() == null || diningTable.getName().isBlank()) {
                        diningTable.setName("Table " + diningTable.getId());
                        logger.debug("Renaming Dining table: {}", diningTable.getName());
                    }

                    return ValidationResults.success(diningTableRepository.update(diningTable));
                } catch(ConcurrencyException e) {
                    errors.add(new ValidationError("This table was modified since it was displayed, please refresh and try again"));
                }
            }
                logger.debug("Validation errors found for Dining Table update {}: {}", diningTable, errors);
                return ValidationResults.invalid(diningTable, errors);

        } catch (Exception e) {
            logger.error("Error updating dining table {}", diningTable, e);
            return ValidationResults.error(diningTable);
        }
    }

    @Override
    public ValidatedResult<DiningTable> delete(Long id) {
        try {
            var tableOptional = diningTableRepository.get(id);

            if (tableOptional.isEmpty()) {
                logger.debug("Dining table with id {} not found", id);
                return ValidationResults.invalid( new DiningTable(), "Seating not found." );
            }

            var diningTable = tableOptional.get();

            boolean hasReservation = diningTableRepository.existReservationForTable(diningTable.getId());
            boolean hasSeating = diningTableRepository.existsSeatingForTable(diningTable.getId());

            if (hasReservation || hasSeating) {
                diningTable.setArchived(true);
                diningTableRepository.update(diningTable);
                logger.debug("Dining Table with id {} has been archived", id);
            } else {
                diningTableRepository.delete(id);
                logger.debug("Dining Table with id {} has been deleted", id);
            }

            return ValidationResults.success();
        } catch (Exception e){
            logger.error("Error deleting dining table with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }
}
