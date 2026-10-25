package ua.conference;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.conference.exception.InvalidDataException;
import ua.conference.model.Event;
import ua.conference.model.EventCategory;
import ua.conference.model.TicketType;
import ua.conference.model.Venue;
import ua.conference.util.ConferenceUtils;
import ua.conference.util.VenueLoader;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class VenueLoaderTest {

    @Test
    @DisplayName("Should successfully create valid Venue object via factory method")
    void testValidVenueCreation() {
        Venue venue = Venue.of("HALL-101", "Conference Center", 250);
        assertEquals("HALL-101", venue.getVenueCode());
        assertEquals("Conference Center", venue.getName());
        assertEquals(250, venue.getCapacity());
    }

    @Test
    @DisplayName("Should successfully parse correct CSV line")
    void testParseValidCsvLine() throws InvalidDataException {
        String line = "HALL-A, Main Center, 300";
        Venue venue = VenueLoader.parseVenueLine(line, 2);
        assertNotNull(venue);
        assertEquals("HALL-A", venue.getVenueCode());
        assertEquals(300, venue.getCapacity());
    }

    @Test
    @DisplayName("Should throw InvalidDataException when capacity is not a number")
    void testInvalidNumberFormatInCsvLine() {
        String line = "HALL-B, Tech Hub, ABC";
        InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
                VenueLoader.parseVenueLine(line, 3)
        );
        assertTrue(exception.getMessage().contains("Line 3"));
    }

    @Test
    @DisplayName("Should throw InvalidDataException when domain validation fails (negative capacity)")
    void testNegativeCapacityInCsvLine() {
        String line = "HALL-C, Science Room, -50";
        InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
                VenueLoader.parseVenueLine(line, 4)
        );
        assertTrue(exception.getMessage().contains("Line 4"));
        assertNotNull(exception.getCause());
    }

    @Test
    @DisplayName("Should correctly calculate ticket price using switch expression")
    void testCalculateTicketPrice() {
        double price = ConferenceUtils.calculateTicketPrice(TicketType.VIP, 1000.0);
        assertEquals(1500.0, price, 0.001);
    }

    @Test
    @DisplayName("Should correctly calculate event duration in days")
    void testEventDaysCalculation() {
        Venue venue = Venue.of("H-1", "Room", 50);
        Event event = new Event("E-1", "Java Fest", venue, EventCategory.TECH,
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 3));
        assertEquals(3, ConferenceUtils.eventDays(event));
    }
}