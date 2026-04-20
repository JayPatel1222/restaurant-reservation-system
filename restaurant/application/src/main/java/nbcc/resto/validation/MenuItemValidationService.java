package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Menu;
import nbcc.resto.dto.MenuItem;
import nbcc.resto.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MenuItemValidationService {
    private final AnnotationValidationService annotationValidationService;
    private final MenuItemRepository menuItemRepository;

    public MenuItemValidationService(AnnotationValidationService annotationValidationService, MenuItemRepository menuItemRepository) {
        this.annotationValidationService = annotationValidationService;
        this.menuItemRepository = menuItemRepository;
    }

    public Collection<ValidationError> validate(MenuItem item,Menu menu) {
        var validationErrors = new ArrayList<ValidationError>();

        validationErrors.addAll(annotationValidationService.validate(item));
        validationErrors.addAll(validateUniqueName(item,menu));

        return validationErrors;
    }

    private Collection<ValidationError> validateUniqueName(MenuItem item, Menu menu) {
        var menuItems = menuItemRepository.getAll(menu.getId());

        if (item.getId() != null) {
            var dbItem = menuItemRepository.get(item.getId()).orElse(null);

            if (dbItem != null && !item.getName().equalsIgnoreCase(dbItem.getName())) {
                if (checkMenuItemExists(menuItems, item.getName())) {
                    return List.of(new ValidationError("Item with this name already present"));
                }
            }
            return List.of();
        }
        if (checkMenuItemExists(menuItems, item.getName())) {
            return List.of(new ValidationError("Item with this name already present"));
        }

        return List.of();
    }

    private boolean checkMenuItemExists(List<MenuItem> items,String name){
        return  items.stream().anyMatch( i
                -> i.getName().equalsIgnoreCase(name));
    }
}
