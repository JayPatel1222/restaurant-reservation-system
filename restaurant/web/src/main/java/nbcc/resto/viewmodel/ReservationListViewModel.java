package nbcc.resto.viewmodel;

import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.dto.UrbanPlatterEvent;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ReservationListViewModel  implements Iterable<UrbanPlatterEvent>{

    private final boolean showReservationsButton;
    private final Collection<UrbanPlatterEvent> events;
    private final Collection<Reservation> reservations;
    private final boolean showEvent;
    private boolean showBackButton;
    private Long filterByEvent;
    private ReservationStatus filterByStatus;
    private String title = "Reservation Requests";
    private boolean hideStatus;

    public ReservationListViewModel(Collection<UrbanPlatterEvent> events, boolean showReservationsButton) {
        this(events, showReservationsButton, new ArrayList<>(), false);
    }
    public ReservationListViewModel(boolean showEvent, Collection<Reservation> reservations) {
        this(new  ArrayList<>(), false, reservations, showEvent);
    }

    public ReservationListViewModel( Collection<UrbanPlatterEvent> events, boolean showReservationsButton, Collection<Reservation> reservations, boolean showEvent) {
        this.showReservationsButton = showReservationsButton;
        this.events = events;
        this.reservations = reservations;
        this.showEvent = showEvent;
    }

    public Collection<Reservation> getReservations() {
        return reservations;
    }
    public Collection<UrbanPlatterEvent> getEvents() {
        return events;
    }
    public boolean isEmpty() {
        return events.isEmpty();
    }
    public boolean isReservationsEmpty() { return reservations.isEmpty(); }
    public boolean isShowEvent() { return showEvent; }
    public boolean isShowReservationsButton() {return showReservationsButton;}

    public Long getFilterByEvent() {
        return filterByEvent;
    }

    public void setFilterByEvent(Long filterByEvent) {
        this.filterByEvent = filterByEvent;
    }

    public ReservationStatus getFilterByStatus() {
        return filterByStatus;
    }

    public void setFilterByStatus(ReservationStatus filterByStatus) {
        this.filterByStatus = filterByStatus;
    }

    public boolean isShowBackButton() {
        return showBackButton;
    }

    public ReservationListViewModel setShowBackButton(boolean showBackButton) {
        this.showBackButton = showBackButton;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ReservationListViewModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isHideStatus() {
        return hideStatus;
    }

    public ReservationListViewModel setHideStatus(boolean hideStatus) {
        this.hideStatus = hideStatus;
        return this;
    }

    @NonNull
    @Override
    public Iterator<UrbanPlatterEvent> iterator() {
        return events.iterator();
    }
}
