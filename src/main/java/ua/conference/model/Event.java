package ua.conference.model;

import ua.conference.util.ConferenceUtils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

public record Event(
        String eventCode,
        String title,
        Venue venue,
        EventCategory category,
        LocalDate startDate,
        LocalDate endDate
) implements Comparable<Event> {

    private static final Comparator<Event> NATURAL_ORDER_COMPARATOR =
            Comparator.comparing(Event::eventCode);

    public Event {
        ConferenceUtils.validateNonNullOrEmpty(eventCode, "EventCode");
        ConferenceUtils.validateNonNullOrEmpty(title, "Title");
        ConferenceUtils.validateNonNull(venue, "Venue");
        ConferenceUtils.validateNonNull(startDate, "StartDate");
        ConferenceUtils.validateNonNull(endDate, "EndDate");

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("EndDate expected >= StartDate, but got: " + endDate + " < " + startDate);
        }
        eventCode = ConferenceUtils.normalizeString(eventCode);
        title = ConferenceUtils.normalizeString(title);
    }

    @Override
    public int compareTo(Event other) {
        if (other == null) {
            return 1;
        }
        return NATURAL_ORDER_COMPARATOR.compare(this, other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;
        return Objects.equals(eventCode, event.eventCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventCode);
    }
}