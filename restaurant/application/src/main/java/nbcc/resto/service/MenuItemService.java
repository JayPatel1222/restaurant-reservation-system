package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.MenuItem;

import java.util.List;

public interface MenuItemService {
    Result<List<MenuItem>> getAll(Long menuId);
    ValidatedResult<MenuItem> get(Long id);
    ValidatedResult<MenuItem> create(MenuItem item);
    ValidatedResult<MenuItem> update(MenuItem item);
    ValidatedResult<Void> delete(Long id);


}
