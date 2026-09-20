package ua.conference.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class FormatHelper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(FORMATTER) : "N/A";
    }
}