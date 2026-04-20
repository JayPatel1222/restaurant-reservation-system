package nbcc.resto.viewmodel;

import nbcc.resto.dto.DiningTable;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Iterator;

public class DiningTableListViewModel implements Iterable<DiningTable>{

    private final String message;

    private final Collection<DiningTable> diningTables;

    public DiningTableListViewModel(Collection<DiningTable> diningTables) {
       this(diningTables, null);
    }

    public DiningTableListViewModel(Collection<DiningTable> diningTables, String message) {
        this.message = message;
        this.diningTables = diningTables;
    }

    public Collection<DiningTable> getDiningTables() {
        return diningTables;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEmpty() {
        return diningTables.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<DiningTable> iterator() {
        return diningTables.iterator();
    }
}
