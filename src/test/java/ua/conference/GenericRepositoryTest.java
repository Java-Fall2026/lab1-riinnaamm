package ua.conference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.conference.exception.DuplicateIdentityException;
import ua.conference.model.Event;
import ua.conference.model.EventCategory;
import ua.conference.model.Participant;
import ua.conference.model.Venue;
import ua.conference.repository.GenericRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GenericRepositoryTest {

    private GenericRepository<Venue> venueRepo;
    private GenericRepository<Event> eventRepo;
    private GenericRepository<Participant> participantRepo;

    @BeforeEach
    void setUp() {
        venueRepo = new GenericRepository<>("VenueTestRepo", Venue::getVenueCode);
        eventRepo = new GenericRepository<>("EventTestRepo", Event::eventCode);
        participantRepo = new GenericRepository<>("ParticipantTestRepo", Participant::getEmail);
    }

    @Test
    @DisplayName("1. Should successfully add item and update size")
    void testAddItem() {
        Venue venue = Venue.of("V-1", "Conference Hall A", 200);
        venueRepo.add(venue);
        assertEquals(1, venueRepo.size());
    }

    @Test
    @DisplayName("2. Should throw DuplicateIdentityException when adding duplicate via add()")
    void testAddDuplicateThrowsException() {
        Venue v1 = Venue.of("V-1", "Hall A", 200);
        Venue v2 = Venue.of("V-1", "Hall B", 150);
        venueRepo.add(v1);

        assertThrows(DuplicateIdentityException.class, () -> venueRepo.add(v2));
        assertEquals(1, venueRepo.size());
    }

    @Test
    @DisplayName("3. Should handle addIfAbsent correctly for new and duplicate items")
    void testAddIfAbsent() {
        Venue v1 = Venue.of("V-1", "Hall A", 200);
        Venue v2 = Venue.of("V-1", "Hall B", 150);

        assertTrue(venueRepo.addIfAbsent(v1));
        assertFalse(venueRepo.addIfAbsent(v2));
        assertEquals(1, venueRepo.size());
    }

    @Test
    @DisplayName("4. Should throw IllegalArgumentException on null item or blank identity")
    void testAddNullOrBlankIdentity() {
        assertThrows(IllegalArgumentException.class, () -> venueRepo.add(null));
    }

    @Test
    @DisplayName("5. Should find existing item and return empty Optional for missing")
    void testFindByIdentity() {
        Venue venue = Venue.of("V-100", "VIP Lounge", 50);
        venueRepo.add(venue);

        Optional<Venue> found = venueRepo.findByIdentity("V-100");
        assertTrue(found.isPresent());
        assertEquals("VIP Lounge", found.get().getName());

        assertTrue(venueRepo.findByIdentity("NON-EXISTENT").isEmpty());
    }

    @Test
    @DisplayName("6. Should remove existing item and return false when removing non-existent item")
    void testRemoveByIdentity() {
        Venue venue = Venue.of("V-50", "Small Room", 20);
        venueRepo.add(venue);

        assertTrue(venueRepo.removeByIdentity("V-50"));
        assertEquals(0, venueRepo.size());
        assertFalse(venueRepo.removeByIdentity("V-50"));
    }

    @Test
    @DisplayName("7. Should guarantee immutability of list returned by findAll()")
    void testFindAllUnmodifiableList() {
        Venue venue = Venue.of("V-1", "Hall A", 200);
        venueRepo.add(venue);

        List<Venue> list = venueRepo.findAll();
        assertEquals(1, list.size());
        assertThrows(UnsupportedOperationException.class, () -> list.add(Venue.of("V-2", "Hall B", 50)));
    }

    @Test
    @DisplayName("8. Should correctly support multiple domain entity types (Venue, Event, Participant)")
    void testMultipleDomainTypes() {
        Venue v = Venue.of("V-1", "Main Hall", 300);
        Event e = new Event("CONF-1", "Tech Conf", v, EventCategory.TECH, LocalDate.now(), LocalDate.now().plusDays(2));
        Participant p = Participant.of("john@example.com", "John Doe", "Tech LLC");

        venueRepo.add(v);
        eventRepo.add(e);
        participantRepo.add(p);

        assertEquals(1, venueRepo.size());
        assertEquals(1, eventRepo.size());
        assertEquals(1, participantRepo.size());

        assertEquals("Tech Conf", eventRepo.findByIdentity("CONF-1").get().title());
        assertEquals("John Doe", participantRepo.findByIdentity("john@example.com").get().getFullName());
    }
}