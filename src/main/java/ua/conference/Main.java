package ua.conference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.conference.model.Event;
import ua.conference.model.EventCategory;
import ua.conference.model.Venue;
import ua.conference.repository.GenericRepository;
import ua.conference.util.VenueLoader;

import java.time.LocalDate;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println(" 1. GenericRepository for Venue Objects ");
        GenericRepository<Venue> venueRepository = new GenericRepository<>(Venue::getVenueCode);

        Venue v1 = Venue.of("HALL-101", "Main Center", 250);
        Venue v2 = Venue.of("HALL-102", "Tech Room", 100);
        Venue duplicateV1 = Venue.of("HALL-101", "Duplicate Hall", 300);

        venueRepository.add(v1);
        venueRepository.add(v2);

        System.out.println("\n Testing Duplicate Addition ");
        boolean isAdded = venueRepository.add(duplicateV1); // Duplicate identity HALL-101
        System.out.println("Duplicate added status: " + isAdded);

        System.out.println("\n Search by Identity ");
        venueRepository.findByIdentity("HALL-101")
                .ifPresentOrElse(
                        v -> System.out.println("Found venue: " + v),
                        () -> System.out.println("Venue not found")
                );

        System.out.println("\n 2. GenericRepository for Event Objects ");
        GenericRepository<Event> eventRepository = new GenericRepository<>(Event::eventCode);

        Event e1 = new Event("CONF-2026", "Java Tech Summit", v1, EventCategory.TECH,
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 3));
        Event e2 = new Event("CONF-2027", "AI Innovations", v2, EventCategory.SCIENCE,
                LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 12));

        eventRepository.add(e1);
        eventRepository.add(e2);

        System.out.println("All Events in repository:");
        eventRepository.findAll().forEach(System.out::println);

        System.out.println("\n 3. Loading Venues from CSV into Repository ");
        VenueLoader.loadVenues("data/venues.csv").forEach(venueRepository::add);
        System.out.println("Total venues in repository after CSV load: " + venueRepository.findAll().size());
    }
}