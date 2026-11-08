package ua.conference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.conference.exception.DuplicateIdentityException;
import ua.conference.model.Event;
import ua.conference.model.EventCategory;
import ua.conference.model.Participant;
import ua.conference.model.Venue;
import ua.conference.repository.GenericRepository;
import ua.conference.util.VenueLoader;

import java.time.LocalDate;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("=== 1. GenericRepository for Venue (Mutable Class with Setters) ===");
        GenericRepository<Venue> venueRepository = new GenericRepository<>("VenueRepo", Venue::getVenueCode);

        Venue v1 = Venue.of("HALL-101", "Main Center", 250);
        Venue v2 = Venue.of("HALL-102", "Tech Room", 100);
        venueRepository.add(v1);
        venueRepository.add(v2);

        System.out.println("\n--- Demonstration: add() throws DuplicateIdentityException (ERROR level) ---");
        try {
            Venue duplicateV1 = Venue.of("HALL-101", "Duplicate Hall", 300);
            venueRepository.add(duplicateV1);
        } catch (DuplicateIdentityException e) {
            log.error("Caught expected exception when adding duplicate via add(): {}", e.getMessage());
        }

        System.out.println("\n--- Demonstration: addIfAbsent() returns false on duplicate ---");
        Venue duplicateV2 = Venue.of("HALL-101", "Another Duplicate", 400);
        boolean isAdded = venueRepository.addIfAbsent(duplicateV2);
        System.out.println("addIfAbsent result for existing identity 'HALL-101': " + isAdded);

        System.out.println("\n=== 2. GenericRepository for Event (Record Domain Type) ===");
        GenericRepository<Event> eventRepository = new GenericRepository<>("EventRepo", Event::eventCode);

        Event e1 = new Event("CONF-2026", "Java Tech Summit", v1, EventCategory.TECH,
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 3));
        Event e2 = new Event("CONF-2027", "AI Innovations", v2, EventCategory.SCIENCE,
                LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 12));

        eventRepository.add(e1);
        eventRepository.add(e2);

        System.out.println("All Events in repository:");
        eventRepository.findAll().forEach(System.out::println);

        System.out.println("\n=== 3. GenericRepository for Participant (3rd Domain Type) ===");
        GenericRepository<Participant> participantRepository = new GenericRepository<>("ParticipantRepo", Participant::getEmail);

        Participant p1 = Participant.of("john.doe@example.com", "John Doe", "Tech Corp");
        Participant p2 = Participant.of("alice.smith@example.com", "Alice Smith", "Science Lab");

        participantRepository.add(p1);
        participantRepository.add(p2);

        System.out.println("\n--- Search Found vs Not Found ---");
        System.out.print("Searching 'john.doe@example.com': ");
        participantRepository.findByIdentity("john.doe@example.com")
                .ifPresentOrElse(
                        p -> System.out.println("FOUND -> " + p),
                        () -> System.out.println("NOT FOUND")
                );

        System.out.print("Searching 'unknown@example.com': ");
        participantRepository.findByIdentity("unknown@example.com")
                .ifPresentOrElse(
                        p -> System.out.println("FOUND -> " + p),
                        () -> System.out.println("NOT FOUND (Expected scenario)")
                );

        System.out.println("\n--- Removal Demonstration ---");
        boolean removed = participantRepository.removeByIdentity("john.doe@example.com");
        System.out.println("Removed 'john.doe@example.com' status: " + removed);
        System.out.println("Participant repository size after removal: " + participantRepository.size());

        System.out.println("\n=== 4. Loading Venues from CSV into Repository ===");
        List<Venue> csvVenues = VenueLoader.loadVenues("data/venues.csv");
        for (Venue csvVenue : csvVenues) {
            boolean addedFromCsv = venueRepository.addIfAbsent(csvVenue);
            if (!addedFromCsv) {
                log.warn("Skipped adding duplicate venue from CSV: {}", csvVenue.getVenueCode());
            }
        }
        System.out.println("Total venues in repository after CSV load: " + venueRepository.size());
    }
}