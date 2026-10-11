package ua.conference.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class FormatHelper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private FormatHelper() {
    }

    static String formatDate(LocalDate date) {
        return date != null ? date.format(FORMATTER) : "N/A";
    }

    static String normalize(String value) {
        return value != null ? value.trim() : null;
    }

    static String normalizeToUpperCase(String value) {
        String trimmed = normalize(value);
        return trimmed != null ? trimmed.toUpperCase() : null;
    }

    static String normalizeToLowerCase(String value) {
        String trimmed = normalize(value);
        return trimmed != null ? trimmed.toLowerCase() : null;
    }
}