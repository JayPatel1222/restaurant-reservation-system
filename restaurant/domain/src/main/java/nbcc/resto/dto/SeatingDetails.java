package nbcc.resto.dto;


import java.time.LocalDateTime;

public class SeatingDetails {
    private Long id;

    private String name;

    private LocalDateTime startDate;

    private Integer duration;

    private Long version;

    private Boolean archived;

    public Long getId() {
        return id;
    }

    public SeatingDetails setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SeatingDetails setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public SeatingDetails setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public Integer getDuration() {
        return duration;
    }

    public SeatingDetails setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public SeatingDetails setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Boolean getArchived() {
        return archived;
    }

    public SeatingDetails setArchived(Boolean archived) {
        this.archived = archived;
        return this;
    }
}
