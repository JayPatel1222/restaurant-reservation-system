package nbcc.resto.validation;

import nbcc.common.result.ValidatedResult;
import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.repository.DiningTableRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class DiningTableValidationService {

    private final AnnotationValidationService annotationValidationService;
    private final DiningTableRepository diningTableRepository;

    public DiningTableValidationService(AnnotationValidationService annotationValidationService, DiningTableRepository diningTableRepository) {
        this.annotationValidationService = annotationValidationService;
        this.diningTableRepository = diningTableRepository;
    }

    public Collection<ValidationError> validate(DiningTable diningTable) {
        var errors = new ArrayList<ValidationError>();

        errors.addAll(annotationValidationService.validate(diningTable));
        errors.addAll(validateUniqueName(diningTable));
        return errors;
    }

    public Collection<ValidationError> validateUniqueName(DiningTable diningTable) {

        if (diningTable.getName() == null || diningTable.getName().isBlank() || diningTable.getName().isEmpty())
            return List.of();

        if (diningTable.getId() != null && diningTable.getId() > 0) {
            var optionalTable = diningTableRepository.get(diningTable.getId());

            if (optionalTable.isPresent()) {
                var dbTable = optionalTable.get();

                if (!diningTable.getName().equalsIgnoreCase(dbTable.getName())) {
                    if (diningTableRepository.exists(diningTable.getName())) {
                        return List.of(new ValidationError("Table with that name already exists.", "name", diningTable.getName()));
                    }
                }
            }
        } else  if (diningTableRepository.exists(diningTable.getName())) {
            return List.of(new ValidationError("Dining table with that name already exists.", "name", diningTable.getName()));
        }

        return List.of();
    }

}