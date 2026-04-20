package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class Seating {


    private Long id;

    @NotNull(message = "Event is required")
    private UrbanPlatterEvent platterEvent;

    @NotBlank(message = "Name is required")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "Start Date and Time is required")
    private LocalDateTime startDate;

    @Positive(message = "Duration must be greater than 0")
    @NotNull(message = "Duration in minutes is required")
    private Integer duration;

    private Long version;

    private Boolean archived;

    public Seating() {}

    public Seating(UrbanPlatterEvent platterEvent) { this.platterEvent = platterEvent; }

    public Seating(Seating seating) {
        this(seating.getId(), seating.getPlatterEvent(), seating.getName(), seating.getStartDate(), seating.getDuration(), seating.getVersion());
    }

    public Seating(Long id, UrbanPlatterEvent platterEvent, String name, LocalDateTime startDate, Integer duration, Long version) {
        this.id = id;
        this.platterEvent = platterEvent;
        this.name = name;
        this.startDate = startDate;
        this.duration = duration;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public Seating setId(Long id) {
        this.id = id;
        return this;
    }

    public UrbanPlatterEvent getPlatterEvent() {
        return platterEvent;
    }

    public Seating setPlatterEvent(UrbanPlatterEvent platterEvent) {
        this.platterEvent = platterEvent;
        return this;
    }

    public Long getPlatterEventId() { return getPlatterEvent() != null ? getPlatterEvent().getId() : null; }

    public String getName() {
        return name;
    }

    public Seating setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public Seating setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public Integer getDuration() {
        return duration;
    }

    public Seating setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public Seating setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Boolean getArchived() {
        return archived;
    }

    public Seating setArchived(Boolean archived) {
        this.archived = archived;
        return this;
    }
}


