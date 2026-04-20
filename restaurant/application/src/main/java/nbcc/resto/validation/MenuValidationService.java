package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Menu;
import nbcc.resto.dto.UrbanPlatterEvent;
import nbcc.resto.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MenuValidationService {
    private final AnnotationValidationService annotationValidationService;
    private final MenuRepository menuRepository;

    public MenuValidationService(AnnotationValidationService annotationValidationService, MenuRepository menuRepository) {
        this.annotationValidationService = annotationValidationService;
        this.menuRepository = menuRepository;
    }

    public Collection<ValidationError> validate(Menu menu) {
        var validationErrors = new ArrayList<ValidationError>();

        validationErrors.addAll(annotationValidationService.validate(menu));
        validationErrors.addAll(validateUniqueName(menu));

        return validationErrors;
    }

    private Collection<ValidationError> validateUniqueName(Menu menu){

        if(menu.getId() != null) {
            var dbMenu = menuRepository.get(menu.getId());

            if(dbMenu.isPresent() && !menu.getName().equalsIgnoreCase(dbMenu.get().getName())){

                var updatedMenuNameExists = menuRepository.exists(menu.getName());

                if(updatedMenuNameExists) {
                    return List.of(new ValidationError("Menu with this name already exists," +
                            " Please select different name", "name"));
                }
            }
            return List.of();
        }
        var menuNameExists = menuRepository.exists(menu.getName());

        if (menuNameExists) {
            return List.of(new ValidationError("Menu with this name already exists," +
                    " Please select different name", "name"));
        }
        return List.of();
    }
}
