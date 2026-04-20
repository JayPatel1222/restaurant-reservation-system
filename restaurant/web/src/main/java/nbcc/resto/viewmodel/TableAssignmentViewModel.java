package nbcc.resto.viewmodel;

import nbcc.resto.dto.TableAssignment;

import java.util.Collection;
import java.util.Iterator;

public class TableAssignmentViewModel implements Iterable<TableAssignment> {

    private final Collection<TableAssignment> tableAssignments;

    public TableAssignmentViewModel(Collection<TableAssignment> tableAssignments) {
        this.tableAssignments = tableAssignments;
    }

    public Collection<TableAssignment> getTableAssignments() { return tableAssignments; }
    public boolean isEmpty() { return tableAssignments.isEmpty(); }

    @Override
    public Iterator<TableAssignment> iterator() {
        return tableAssignments.iterator();
    }


}
