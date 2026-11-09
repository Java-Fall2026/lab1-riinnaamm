package ua.conference.model;

import ua.conference.util.ConferenceUtils;
import java.time.LocalDate;

public record Event(
        String eventCode,
        String title,
        Venue venue,
        EventCategory category,
        LocalDate startDate,
        LocalDate endDate
) implements Comparable<Event> {

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
        int codeComparison = this.eventCode.compareTo(other.eventCode);
        if (codeComparison != 0) {
            return codeComparison;
        }

        int dateComparison = this.startDate.compareTo(other.startDate);
        if (dateComparison != 0) {
            return dateComparison;
        }

        return this.title.compareTo(other.title);
    }
}