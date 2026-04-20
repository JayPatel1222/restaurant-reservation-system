package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.MenuItem;
import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {
    List<MenuItem> getAll(Long menuId);
    Optional<MenuItem> get(Long id);
    MenuItem create(MenuItem item);
    MenuItem update(MenuItem item) throws ConcurrencyException;
    void delete(Long id);
}
