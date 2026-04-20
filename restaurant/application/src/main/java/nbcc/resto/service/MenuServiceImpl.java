package nbcc.resto.service;

import nbcc.common.exception.ConcurrencyException;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Menu;
import nbcc.resto.repository.MenuRepository;
import nbcc.resto.validation.MenuValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MenuServiceImpl implements MenuService{

    private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
    private final MenuRepository menuRepository;
    private final MenuValidationService menuValidationService;

    public MenuServiceImpl(MenuRepository menuRepository, MenuValidationService menuValidationService) {
        this.menuRepository = menuRepository;
        this.menuValidationService = menuValidationService;
    }

    @Override
    public Result<Collection<Menu>> getAll() {
        try{
            return ValidationResults.success(menuRepository.getAll());
        }catch (Exception e){
            logger.error("Error retrieving all menus",e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Menu> get(Long id) {
        try{
            return ValidationResults.success(menuRepository.get(id));
        }catch (Exception e){
            logger.error("Error retrieving menu with id {}",id,e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Menu> create(Menu menu) {
        try{
            var errors = menuValidationService.validate(menu);
            if(errors.isEmpty()) {
                return ValidationResults.success(menuRepository.create(menu));
            }
            return ValidationResults.invalid(menu,errors);
        }catch (Exception e){
            logger.error("Error creating menu",e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Menu> update(Menu menu) {
        try{
            var errors = menuValidationService.validate(menu);
            if(errors.isEmpty()) {
                try {
                    return ValidationResults.success(menuRepository.update(menu));
                } catch (ConcurrencyException e) {
                    errors.add(new ValidationError("Menu was modified since it " +
                            "was displayed, please refresh and try again "));
                }
            }
            return ValidationResults.invalid(menu,errors);
        }catch (Exception e){
            logger.error("Error updating menu",e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Void> delete(Long id) {
        try{
            menuRepository.delete(id);
            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Error deleting menu with id {}",id,e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<Menu>> search(String name) {
        try {
            return ValidationResults.success(menuRepository.search(name));
        } catch (Exception e){
            logger.error("Error deleting menu with name {}",name,e);
            return ValidationResults.error(e);
        }
    }
}
