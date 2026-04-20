package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.DiningTable;

import java.util.List;

public interface DiningTableService {
    Result<List<DiningTable>> getAll();
    ValidatedResult<DiningTable> get(Long id);
    ValidatedResult<DiningTable> create(DiningTable diningTable);
    ValidatedResult<DiningTable> update(DiningTable diningTable);
    ValidatedResult<DiningTable> delete(Long id);
}
