package nbcc.resto.dto;

import java.util.Collection;

public class EventDetails {
   private UrbanPlatterEvent event;
   private Collection<SeatingDetails> seatings;

    public UrbanPlatterEvent getEvent() {
        return event;
    }

    public EventDetails setEvent(UrbanPlatterEvent event) {
        this.event = event;
        return this;
    }

    public Collection<SeatingDetails> getSeatings() {
        return seatings;
    }

    public EventDetails setSeatings(Collection<SeatingDetails> seatings) {
        this.seatings = seatings;
        return this;
    }
}
