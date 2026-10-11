package ua.conference.util;

import java.util.List;

class ValidationHelper {

    private ValidationHelper() {
    }

    static void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " expected non-null object, but got: null");
        }
    }

    static void requireNonNullOrEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " expected non-empty string, but got: " + value);
        }
    }

    static void requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " expected > 0, but got: " + value);
        }
    }

    static void requireAllowedValue(String value, List<String> allowedValues, String fieldName) {
        requireNonNullOrEmpty(value, fieldName);
        if (!allowedValues.contains(value)) {
            throw new IllegalArgumentException(fieldName + " expected one of " + allowedValues + ", but got: " + value);
        }
    }
}