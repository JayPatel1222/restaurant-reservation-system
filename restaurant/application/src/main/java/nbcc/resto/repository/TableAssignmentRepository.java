package nbcc.resto.repository;

import nbcc.resto.dto.DiningTable;
import nbcc.resto.dto.TableAssignment;

import java.util.Collection;

public interface TableAssignmentRepository {
    TableAssignment assignTable(TableAssignment tableAssignment);
    Collection<TableAssignment> getByTableId(Long tableId);
    Collection<TableAssignment> getBySeatingId(Long seatingId);
}
