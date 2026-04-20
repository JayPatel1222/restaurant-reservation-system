package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.TableAssignment;
import nbcc.resto.repository.DiningTableRepository;
import nbcc.resto.repository.TableAssignmentRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class TableAssignmentValidationService {

    private final AnnotationValidationService validationService;
    private final TableAssignmentRepository assignmentRepository;
    private final DiningTableRepository tableRepository;

    public TableAssignmentValidationService(AnnotationValidationService validationService, TableAssignmentRepository assignmentRepository, DiningTableRepository tableRepository) {
        this.validationService = validationService;
        this.assignmentRepository = assignmentRepository;
        this.tableRepository = tableRepository;
    }

    public Collection<ValidationError> validate(TableAssignment tableAssignment) {

        var errors = new ArrayList<ValidationError>();

        errors.addAll(validationService.validate(tableAssignment));
        errors.addAll(validateTableExists(tableAssignment));
        errors.addAll(validateNotAlreadyAssigned(tableAssignment));

        return errors;
    }

    public Collection<ValidationError> validateTableExists(TableAssignment tableAssignment){

        var diningTable = tableAssignment.getDiningTable();

        if (diningTable == null){
            return List.of(new ValidationError("Valid Table Not Specified"));
        }

        if (tableRepository.get(diningTable.getId()).isEmpty()){
            return List.of(new ValidationError("Table with id " + diningTable.getId() + " does not exist"));
        }

        return List.of();
    }

    public Collection<ValidationError> validateNotAlreadyAssigned(TableAssignment tableAssignment) {

        if (tableAssignment.getDiningTable() == null) {
            return List.of();
        }

        if (tableAssignment.getSeating() == null) {
            return List.of(new ValidationError("Valid Seating Not Specified"));
        }

        var diningTable = tableAssignment.getDiningTable();
        var seating =  tableAssignment.getSeating();
        var assignments = assignmentRepository.getByTableId(diningTable.getId());

        if ( assignments != null && !assignments.isEmpty()) {

            LocalDateTime seatingEndDate = seating.getStartDate().plusMinutes(seating.getDuration());
            LocalDateTime existingSeatingEndDate;

            for (var assignment : assignments) {
                existingSeatingEndDate = assignment.getSeating().getStartDate()
                        .plusMinutes(assignment.getSeating().getDuration());

                if ( seating.getStartDate().isBefore(existingSeatingEndDate) &&
                    assignment.getSeating().getStartDate().isBefore(seatingEndDate)) {
                    return List.of(new ValidationError("Table " + diningTable.getName() + " is already in use."));
                }
            }
        }

        return List.of();
    }
}
