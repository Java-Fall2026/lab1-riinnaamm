package ua.conference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.conference.model.Venue;
import ua.conference.repository.GenericRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GenericRepositoryTest {

    private GenericRepository<Venue> repository;

    @BeforeEach
    void setUp() {
        repository = new GenericRepository<>(Venue::getVenueCode);
    }

    @Test
    @DisplayName("Should successfully add unique items to repository")
    void testAddItem() {
        Venue venue = Venue.of("V-1", "Conference Hall A", 200);
        assertTrue(repository.add(venue));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    @DisplayName("Should reject adding duplicate item with existing identity")
    void testAddDuplicateItem() {
        Venue v1 = Venue.of("V-1", "Hall A", 200);
        Venue v2 = Venue.of("V-1", "Hall B", 150);

        assertTrue(repository.add(v1));
        assertFalse(repository.add(v2));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    @DisplayName("Should find item by correct identity")
    void testFindByIdentity() {
        Venue venue = Venue.of("V-100", "VIP Lounge", 50);
        repository.add(venue);

        Optional<Venue> found = repository.findByIdentity("V-100");
        assertTrue(found.isPresent());
        assertEquals("VIP Lounge", found.get().getName());
    }

    @Test
    @DisplayName("Should return empty Optional when identity is not found")
    void testFindByIdentityNotFound() {
        Optional<Venue> found = repository.findByIdentity("NON-EXISTENT");
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Should successfully remove item by identity")
    void testRemoveByIdentity() {
        Venue venue = Venue.of("V-50", "Small Room", 20);
        repository.add(venue);

        assertTrue(repository.removeByIdentity("V-50"));
        assertEquals(0, repository.findAll().size());
    }
}