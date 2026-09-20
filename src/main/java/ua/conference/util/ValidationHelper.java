package ua.conference.util;

public class ValidationHelper {
    public static void requireNonNullOrEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " expected non-empty string, but got: " + value);
        }
    }

    public static void requireLowerCase(String value, String fieldName) {
        if (value != null && !value.equals(value.toLowerCase())) {
            throw new IllegalArgumentException(fieldName + " expected lowercase string, but got: " + value);
        }
    }

    public static void requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " expected > 0, but got: " + value);
        }
    }

    public static void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " expected non-null object, but got: null");
        }
    }
}