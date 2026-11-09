package ua.conference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.conference.model.Event;
import ua.conference.model.EventCategory;
import ua.conference.model.Venue;
import ua.conference.repository.EventRepository;
import ua.conference.repository.SortOrder;
import ua.conference.repository.VenueRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class Lab5RepositoryTest {

    private VenueRepository venueRepository;
    private EventRepository eventRepository;

    private Venue v1;
    private Venue v2;
    private Venue v3;

    @BeforeEach
    void setUp() {
        venueRepository = new VenueRepository();
        eventRepository = new EventRepository();

        v1 = Venue.of("V01", "Alpha Hall", 100);
        v2 = Venue.of("V02", "Beta Center", 300);
        v3 = Venue.of("V03", "Gamma Room", 50);

        venueRepository.add(v1);
        venueRepository.add(v2);
        venueRepository.add(v3);
    }

    @Test
    @DisplayName("1. Natural order: Collections.sort sorts elements by Comparable interface")
    void testNaturalOrderSorting() {
        List<Venue> list = new ArrayList<>(List.of(v2, v1, v3));
        Collections.sort(list);
        assertEquals(List.of(v1, v2, v3), list);
    }

    @Test
    @DisplayName("2. Multi-level comparator: sorts by capacity DESC then by name ASC")
    void testMultiLevelComparator() {
        Venue v4 = Venue.of("V04", "Abc Hall", 300);
        venueRepository.add(v4);

        List<Venue> sorted = venueRepository.findAllSortedByCapacityDescThenName();

        assertEquals("Abc Hall", sorted.get(0).getName());
        assertEquals("Beta Center", sorted.get(1).getName());
    }

    @Test
    @DisplayName("3. findAllSortedByIdentity in ASC and DESC give reverse lists")
    void testFindAllSortedByIdentityAscAndDesc() {
        List<Venue> asc = venueRepository.findAllSortedByIdentity(SortOrder.ASC);
        List<Venue> desc = venueRepository.findAllSortedByIdentity(SortOrder.DESC);

        List<Venue> descReversed = new ArrayList<>(desc);
        Collections.reverse(descReversed);

        assertEquals(asc, descReversed);
    }

    @Test
    @DisplayName("4. Two objects with capacity difference are sorted correctly")
    void testCapacityComparison() {
        Venue small = Venue.of("V10", "Small", 10);
        Venue big = Venue.of("V11", "Big", 11);

        assertTrue(small.compareTo(big) < 0);
        assertTrue(Integer.compare(small.getCapacity(), big.getCapacity()) < 0);
    }

    @Test
    @DisplayName("5. TreeSet with custom comparator (inconsistent with equals) filters out duplicate key values")
    void testTreeSetInconsistentComparator() {
        Comparator<Venue> capacityComparator = Comparator.comparing(Venue::getCapacity);
        Set<Venue> treeSet = new TreeSet<>(capacityComparator);

        Venue vA = Venue.of("CODE1", "Hall A", 200);
        Venue vB = Venue.of("CODE2", "Hall B", 200); // Same capacity, different code

        treeSet.add(vA);
        treeSet.add(vB);

        assertEquals(1, treeSet.size());
        assertFalse(vA.equals(vB));
    }

    @Test
    @DisplayName("6. Search methods filter correctly by range and dual condition")
    void testSearchMethods() {
        List<Venue> inRange = venueRepository.findByCapacityRange(60, 350);
        assertEquals(2, inRange.size());
        assertFalse(inRange.contains(v3));

        List<Venue> dualSearch = venueRepository.findLargeVenuesWithName(200, "Beta");
        assertEquals(1, dualSearch.size());
        assertEquals(v2, dualSearch.get(0));
    }

    @Test
    @DisplayName("7. Search method returns Optional.empty() when no elements match")
    void testFindOneReturnsEmptyOptional() {
        Optional<Venue> result = venueRepository.findFirstWithCapacityAtLeast(1000);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("8. groupingBy returns expected number of groups and group size")
    void testGroupingBy() {
        LocalDate now = LocalDate.now();
        Event e1 = new Event("E01", "Java Conf", v1, EventCategory.TECH, now, now.plusDays(1));
        Event e2 = new Event("E02", "Spring Workshop", v2, EventCategory.SCIENCE, now, now.plusDays(1));
        Event e3 = new Event("E03", "AI Conf", v3, EventCategory.TECH, now, now.plusDays(2));

        eventRepository.add(e1);
        eventRepository.add(e2);
        eventRepository.add(e3);

        Map<EventCategory, List<Event>> grouped = eventRepository.groupByCategory();

        assertEquals(2, grouped.size());
        assertEquals(2, grouped.get(EventCategory.TECH).size());
    }

    @Test
    @DisplayName("9. Aggregation on empty repository does not throw exception and returns default")
    void testAggregationOnEmptyRepository() {
        VenueRepository emptyRepo = new VenueRepository();
        assertEquals(0.0, emptyRepo.getAverageCapacity(), 0.001);
    }

    @Test
    @DisplayName("10. Reusing consumed stream throws IllegalStateException")
    void testReusingStreamThrowsException() {
        var stream = venueRepository.stream();
        stream.toList();

        assertThrows(IllegalStateException.class, stream::toList);
    }
}