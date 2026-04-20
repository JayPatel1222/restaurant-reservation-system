package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.dto.TableAssignment;
import nbcc.resto.repository.TableAssignmentRepository;
import nbcc.resto.validation.TableAssignmentValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TableAssignmentServiceImpl implements TableAssignmentService {


    private final Logger logger = LoggerFactory.getLogger(TableAssignmentServiceImpl.class);

    private final SeatingService seatingService;
    private final TableAssignmentRepository assignmentRepository;
    private final TableAssignmentValidationService validationService;
    private final DiningTableService tableService;

    public TableAssignmentServiceImpl(SeatingService seatingService, TableAssignmentRepository assignmentRepository, TableAssignmentValidationService validationService, DiningTableService tableService) {
        this.seatingService = seatingService;
        this.assignmentRepository = assignmentRepository;
        this.validationService = validationService;
        this.tableService = tableService;
    }

    @Override
    public ValidatedResult<TableAssignment> assignTable(Long tableId, Long seatingId) {
        var assignment = new TableAssignment(seatingId, tableId);

        var dbSeatingResult = seatingService.get(seatingId);
        if (dbSeatingResult.isSuccessful() && dbSeatingResult.hasValue()) {
            assignment.setSeating(dbSeatingResult.getValue());
        }

        var dbTableResult = tableService.get(tableId);
        if (dbTableResult.isSuccessful() && dbTableResult.hasValue()) {
            assignment.setDiningTable(dbTableResult.getValue());
        }

        try {
            var errors = validationService.validate(assignment);

            if (errors.isEmpty()) {
                return ValidationResults.success(assignmentRepository.assignTable(assignment));
            } else {
                logger.debug("Validation errors for table assignment {}: {}", assignment, errors);
                return ValidationResults.invalid(assignment, errors);
            }
        } catch (Exception e) {
            logger.error("Error creating table assignment {}", assignment, e);
            return ValidationResults.error(assignment);
        }

    }

    @Override
    public Result<Collection<DiningTable>> getAll(Long seatingId) {
        try {
            var assignments = assignmentRepository.getBySeatingId(seatingId);
            var diningTables = assignments.stream().map(TableAssignment::getDiningTable).toList();

            return ValidationResults.success(diningTables);
        } catch (Exception e) {
            logger.error("Error retrieving tables for this seating", e);
            return ValidationResults.error(e);
        }
    }
}
