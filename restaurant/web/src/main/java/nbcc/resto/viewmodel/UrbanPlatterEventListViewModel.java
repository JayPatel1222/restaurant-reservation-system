package nbcc.resto.viewmodel;

import nbcc.resto.dto.UrbanPlatterEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

public class UrbanPlatterEventListViewModel {

    private final Collection<UrbanPlatterEvent> events;

    private boolean showBackButton;

    private String eventName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime filterStartDate;

    private LocalDateTime filterEndDate;

    private boolean textFlag;



    public UrbanPlatterEventListViewModel(Collection<UrbanPlatterEvent> events) {
        this.events = events;
    }

    public Collection<UrbanPlatterEvent> getEvents() {
        return events;
    }

    public boolean isShowBackButton() {
        return showBackButton;
    }

    public void setShowBackButton(boolean showBackButton) {
        this.showBackButton = showBackButton;
    }

    public String getEventName() {
        return eventName;
    }

    public UrbanPlatterEventListViewModel setEventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public LocalDateTime getFilterStartDate() {
        return filterStartDate;
    }

    public UrbanPlatterEventListViewModel setFilterStartDate(LocalDateTime filterStartDate) {
        this.filterStartDate = filterStartDate;
        return this;
    }

    public LocalDateTime getFilterEndDate() {
        return filterEndDate;
    }

    public void setFilterEndDate(LocalDateTime filterEndDate) {
        this.filterEndDate = filterEndDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isTextFlag() {
        return textFlag;
    }

    public void setTextFlag(boolean textFlag) {
        this.textFlag = textFlag;
    }

    public int getResultCount() {
        return events.size();
    }

    public boolean isEmpty(){
        return events.isEmpty();
    }
}
