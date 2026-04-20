package nbcc.resto.viewmodel;

import nbcc.resto.dto.UrbanPlatterEvent;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SeatingEditViewModel implements Iterable<UrbanPlatterEvent> {

    private final Collection<UrbanPlatterEvent> events;

    public SeatingEditViewModel(Collection<UrbanPlatterEvent> events) {
        this.events = events;
    }

    public Collection<UrbanPlatterEvent> getEvents() {
        return events;
    }
    public boolean isEmpty() {
        return events.isEmpty();
    }


    @Override
    public Iterator<UrbanPlatterEvent> iterator() {
        return events.iterator();
    }
}
