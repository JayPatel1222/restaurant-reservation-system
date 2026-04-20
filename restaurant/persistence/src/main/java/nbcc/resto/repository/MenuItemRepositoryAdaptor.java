package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.MenuItem;
import nbcc.resto.mapper.MenuItemMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MenuItemRepositoryAdaptor implements MenuItemRepository {
    private final MenuItemMapper menuItemMapper ;
    private final MenuItemJPARepository menuItemJPARepository;

    public MenuItemRepositoryAdaptor(MenuItemMapper menuItemMapper, MenuItemJPARepository menuItemJPARepository) {
        this.menuItemMapper = menuItemMapper;
        this.menuItemJPARepository = menuItemJPARepository;
    }

    @Override
    public List<MenuItem> getAll(Long menuId) {
        var entities = menuItemJPARepository.findAllByMenuId(menuId);
        return menuItemMapper.toDTO(entities);
    }

    @Override
    public Optional<MenuItem> get(Long id) {
        var entity = menuItemJPARepository.findById(id);
        return menuItemMapper.toDTO(entity);
    }

    @Override
    public MenuItem create(MenuItem item) {
        var entity = menuItemMapper.toEntity(item);
        entity = menuItemJPARepository.save(entity);
        return menuItemMapper.toDTO(entity);
    }

    @Override
    public MenuItem update(MenuItem item) throws ConcurrencyException {
       try{
           var entity = menuItemMapper.toEntity(item);
           entity = menuItemJPARepository.save(entity);
           return menuItemMapper.toDTO(entity);
       }catch (ConcurrencyFailureException e){
           throw new ConcurrencyException(e);
       }
    }

    @Override
    public void delete(Long id) {
        menuItemJPARepository.deleteById(id);
    }
}
