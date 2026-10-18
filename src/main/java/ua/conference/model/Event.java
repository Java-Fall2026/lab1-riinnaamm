package ua.conference.model;

import ua.conference.util.ConferenceUtils;
import java.time.LocalDate;
import java.util.Objects;

public record Event(
    String eventCode,
    String title,
    Venue venue,
    EventCategory category,
    LocalDate startDate,
    LocalDate endDate
){
    public Event{
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
}