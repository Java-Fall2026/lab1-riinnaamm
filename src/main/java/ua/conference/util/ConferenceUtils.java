package ua.conference.util;

import ua.conference.model.Event;
import ua.conference.model.EventRegistration;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ConferenceUtils {

    public static final List<String> ALLOWED_CATEGORIES = List.of("TECH", "SCIENCE", "BUSINESS", "ART");
    public static final List<String> ALLOWED_TICKET_TYPES = List.of("STANDARD", "VIP", "SPEAKER", "PRESS");

    private ConferenceUtils() {
    }

    public static void validateNonNull(Object value, String fieldName) {
        ValidationHelper.requireNonNull(value, fieldName);
    }

    public static void validateNonNullOrEmpty(String value, String fieldName) {
        ValidationHelper.requireNonNullOrEmpty(value, fieldName);
    }

    public static void validatePositive(int value, String fieldName) {
        ValidationHelper.requirePositive(value, fieldName);
    }

    public static void validateCategory(String category) {
        ValidationHelper.requireAllowedValue(category, ALLOWED_CATEGORIES, "Category");
    }

    public static void validateTicketType(String ticketType) {
        ValidationHelper.requireAllowedValue(ticketType, ALLOWED_TICKET_TYPES, "TicketType");
    }

    public static String normalizeString(String value) {
        return FormatHelper.normalize(value);
    }

    public static String normalizeEmail(String email) {
        return FormatHelper.normalizeToLowerCase(email);
    }

    public static String normalizeEnumString(String value) {
        return FormatHelper.normalizeToUpperCase(value);
    }

    public static long eventDays(Event event) {
        ValidationHelper.requireNonNull(event, "Event");
        return ChronoUnit.DAYS.between(event.getStartDate(), event.getEndDate()) + 1;
    }

    public static long daysBeforeEvent(EventRegistration reg) {
        ValidationHelper.requireNonNull(reg, "EventRegistration");
        return ChronoUnit.DAYS.between(reg.getRegistrationDate(), reg.getEvent().getStartDate());
    }
}