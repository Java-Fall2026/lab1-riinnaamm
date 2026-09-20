package ua.conference.model;

import ua.common.BaseEntity;
import ua.conference.util.ValidationHelper;
import java.time.LocalDate;
import java.util.Objects;

public class Event extends BaseEntity {
    private String eventCode;
    private String title;
    private Venue venue;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;

    public Event(String eventCode, String title, Venue venue, String category, LocalDate startDate, LocalDate endDate) {
        super();
        ValidationHelper.requireNonNullOrEmpty(eventCode, "EventCode");
        ValidationHelper.requireNonNullOrEmpty(title, "Title");
        ValidationHelper.requireNonNull(venue, "Venue");
        ValidationHelper.requireNonNullOrEmpty(category, "Category");
        ValidationHelper.requireNonNull(startDate, "StartDate");
        ValidationHelper.requireNonNull(endDate, "EndDate");

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("EndDate expected >= StartDate, but got: " + endDate + " < " + startDate);
        }

        this.eventCode = eventCode;
        this.title = title;
        this.venue = venue;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getEventCode() { return eventCode; }
    public String getTitle() { return title; }
    public Venue getVenue() { return venue; }
    public String getCategory() { return category; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventCode, event.eventCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventCode);
    }

    @Override
    public String toString() {
        return "Event{" + "eventCode='" + eventCode + '\'' + ", title='" + title + '\'' + '}';
    }
}