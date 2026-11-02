package ua.conference;

import ua.conference.model.Venue;
import ua.conference.util.VenueLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println(" 1. Reading CSV File (With Invalid Lines) ");
        log.info("Starting CSV reading process...");

        List<Venue> venues = VenueLoader.loadVenues("data/venues.csv");
        System.out.println("Loaded Venue objects:");
        for (Venue venue : venues) {
            System.out.println(venue);
        }

        System.out.println("\n 2. Reading Non-Existent File ");
        List<Venue> emptyList = VenueLoader.loadVenues("data/non_existent_file.csv");
        System.out.println("Count of loaded objects from non-existent file: " + emptyList.size());

        System.out.println("\n 3. Testing Setter Validation Error ");
        try {
            Venue testVenue = Venue.of("TEST-1", "Test Room", 100);
            System.out.println("Trying to set invalid capacity (-10)...");
            testVenue.setCapacity(-10);
        } catch (IllegalArgumentException e) {
            log.error("Caught validation error when calling setter: {}", e.getMessage(), e);
            System.out.println("Handled setter exception: " + e.getMessage());
        }
    }
}