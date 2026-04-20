package nbcc.resto.viewmodel;

import nbcc.resto.dto.Seating;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Iterator;

public class SeatingListViewModel implements Iterable<Seating>{


    private final Collection<Seating> seatings;
    private final Long eventId;
    private final boolean showEvent;
    private final boolean showSelectButton;

    public SeatingListViewModel(Collection<Seating> seatings, Long eventId, boolean showEvent) {
        this(seatings, eventId, showEvent, false);
    }

    public SeatingListViewModel(Collection<Seating> seatings, Long eventId, boolean showEvent,  boolean showSelectButton) {
        this.seatings = seatings;
        this.eventId = eventId;
        this.showEvent = showEvent;
        this.showSelectButton = showSelectButton;
    }

    public SeatingListViewModel(Collection<Seating> seatings) {
        this(seatings, null, true);
    }

    public Collection<Seating> getSeatings() {
        return seatings;
    }

    public Long getEventId() {
        return eventId;
    }

    public boolean isShowEvent() { return showEvent; }
    public boolean isShowSelectButton() { return showSelectButton; }

    public boolean isEmpty() {
        return seatings.isEmpty();
    }
    @NonNull
    @Override
    public Iterator<Seating> iterator() {
        return seatings.iterator();
    }
}
