package ua.conference.model;

import ua.common.BaseEntity;
import ua.conference.util.ConferenceUtils;
import java.util.Objects;

public class Venue extends BaseEntity {
    private String venueCode;
    private String name;
    private int capacity;

    private Venue(String venueCode, String name, int capacity) {
        super();
        setVenueCode(venueCode);
        setName(name);
        setCapacity(capacity);
    }

    public static Venue of(String venueCode, String name, int capacity) {
        return new Venue(venueCode, name, capacity);
    }

    public String getVenueCode() { return venueCode; }
    public void setVenueCode(String venueCode) {
        ConferenceUtils.validateNonNullOrEmpty(venueCode, "VenueCode");
        this.venueCode = ConferenceUtils.normalizeString(venueCode);
    }

    public String getName() { return name; }
    public void setName(String name) {
        ConferenceUtils.validateNonNullOrEmpty(name, "Name");
        this.name = ConferenceUtils.normalizeString(name);
    }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) {
        ConferenceUtils.validatePositive(capacity, "Capacity");
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return Objects.equals(venueCode, venue.venueCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(venueCode);
    }

    @Override
    public String toString() {
        return "Venue{" +
                "venueCode='" + venueCode + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", createdAt=" + createdAt +
                '}';
    }
}