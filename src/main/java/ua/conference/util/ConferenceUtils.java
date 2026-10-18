package ua.conference.util;

import ua.conference.model.Event;
import ua.conference.model.EventCategory;
import ua.conference.model.EventRegistration;
import ua.conference.model.TicketType;
import java.time.temporal.ChronoUnit;

public class ConferenceUtils {

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

    public static String normalizeString(String value) {
        return FormatHelper.normalize(value);
    }

    public static String normalizeEmail(String email) {
        return FormatHelper.normalizeToLowerCase(email);
    }

    public static long eventDays(Event event) {
        ValidationHelper.requireNonNull(event, "Event");
        return ChronoUnit.DAYS.between(event.startDate(), event.endDate()) + 1;
    }

    public static long daysBeforeEvent(EventRegistration reg) {
        ValidationHelper.requireNonNull(reg, "EventRegistration");
        return ChronoUnit.DAYS.between(reg.registrationDate(), reg.event().startDate());
    }

    public static String getCategoryDescription(EventCategory category) {
        ValidationHelper.requireNonNull(category, "Category");
        return switch (category) {
            case TECH -> "Information technology, programming, and software architecture.";
            case SCIENCE -> "Academic research, innovations, and scientific publications.";
            case BUSINESS -> "Entrepreneurship, marketing, management, and startups.";
            case ART -> "Design, digital creative arts, and media production.";
        };
    }

    public static double calculateTicketPrice(TicketType ticketType, double basePrice) {
        ValidationHelper.requireNonNull(ticketType, "TicketType");
        ValidationHelper.requirePositive((int) basePrice, "BasePrice");

        double multiplier = switch (ticketType) {
            case STANDARD -> 1.0;
            case VIP -> 1.5;
            case PRESS -> 0.5;
            case SPEAKER -> 0.0;
        };

        return basePrice * multiplier;
    }
}