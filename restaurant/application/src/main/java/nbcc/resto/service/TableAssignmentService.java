package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.dto.Seating;
import nbcc.resto.dto.TableAssignment;

import java.util.Collection;

public interface TableAssignmentService {

    ValidatedResult<TableAssignment> assignTable(Long tableId, Long seatingId);
    Result<Collection<DiningTable>> getAll(Long seatingId);

}
