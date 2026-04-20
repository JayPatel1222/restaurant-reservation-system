package nbcc.resto.service;

import nbcc.common.exception.ConcurrencyException;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.MenuItem;
import nbcc.resto.repository.MenuItemRepository;
import nbcc.resto.repository.MenuRepository;
import nbcc.resto.validation.MenuItemValidationService;
import nbcc.resto.validation.MenuValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemServiceImpl implements MenuItemService {
    private final Logger logger = LoggerFactory.getLogger(MenuItemServiceImpl.class);
    private final MenuItemRepository menuItemRepository;
    private final MenuItemValidationService menuItemValidationService;
    private final MenuService menuService;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, MenuItemValidationService menuItemValidationService, MenuService menuService) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemValidationService = menuItemValidationService;
        this.menuService = menuService;
    }

    @Override
    public Result<List<MenuItem>> getAll(Long menuId) {

        try {
            return ValidationResults.success(menuItemRepository.getAll(menuId));
        } catch (Exception e){
            logger.error("Error retrieving all Menu Items", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<MenuItem> get(Long id) {
        try{
            return ValidationResults.success(menuItemRepository.get(id));
        }catch (Exception e){
            logger.error("Error retrieving menuItem with id {}",id,e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<MenuItem> create(MenuItem item) {
        try{
            var menu = menuService.get(item.getMenuId());
            var errors = menuItemValidationService.validate(item,menu.getValue());

            if(errors.isEmpty()){
                return ValidationResults.success(menuItemRepository.create(item));
            }
            logger.debug("Validation errors for MenuItem create {}: {}",item,errors);
            return ValidationResults.invalid(item,errors);

        } catch (Exception e){
            logger.error("Error creating  Menu Item", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<MenuItem> update(MenuItem item) {
        try{
            var menu = menuService.get(item.getMenuId());
            var errors = menuItemValidationService.validate(item,menu.getValue());
            if(errors.isEmpty()) {
                try {
                    return ValidationResults.success(menuItemRepository.update(item));
                } catch (ConcurrencyException e) {
                    errors.add(new ValidationError("MenuItem was modified since it " +
                            "was displayed, please refresh and try again "));
                }
            }
            return ValidationResults.invalid(item,errors);
        }catch (Exception e){
            logger.error("Error updating menuItem",e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Void> delete(Long id) {
        try{
            menuItemRepository.delete(id);
            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Error deleting menuItem with id {}",id,e);
            return ValidationResults.error(e);
        }
    }
}
