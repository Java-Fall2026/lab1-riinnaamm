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
    @DisplayName("4. Repository sorting orders objects with close capacity values correctly")
    void testCloseCapacitySorting() {
        VenueRepository repo = new VenueRepository();
        Venue cap101 = Venue.of("V10", "Hall 101", 101);
        Venue cap100 = Venue.of("V11", "Hall 100", 100);
        Venue cap102 = Venue.of("V12", "Hall 102", 102);

        repo.add(cap101);
        repo.add(cap100);
        repo.add(cap102);

        List<Venue> sorted = repo.findAllSortedByCapacityDescThenName();
        assertEquals(List.of(cap102, cap101, cap100), sorted);
    }

    @Test
    @DisplayName("5. TreeSet with custom comparator (inconsistent with equals) filters out duplicate key values")
    void testTreeSetInconsistentComparator() {
        Comparator<Venue> capacityComparator = Comparator.comparing(Venue::getCapacity);
        Set<Venue> treeSet = new TreeSet<>(capacityComparator);

        Venue vA = Venue.of("CODE1", "Hall A", 200);
        Venue vB = Venue.of("CODE2", "Hall B", 200);

        treeSet.add(vA);
        treeSet.add(vB);

        assertEquals(1, treeSet.size());
        assertNotEquals(vA, vB);
    }

    @Test
    @DisplayName("6. Venue search methods: text match, range, and dual condition exclude partial matches")
    void testVenueSearchMethods() {
        // Object matching ONLY name condition ("Beta"), but failing capacity (>= 200)
        Venue betaSmall = Venue.of("V04", "Beta Small", 50);
        venueRepository.add(betaSmall);

        // Text search
        List<Venue> byName = venueRepository.findByNameContaining("Beta");
        assertEquals(2, byName.size());
        assertTrue(byName.containsAll(List.of(v2, betaSmall)));

        // Range search
        List<Venue> inRange = venueRepository.findByCapacityRange(60, 350);
        assertEquals(2, inRange.size());
        assertFalse(inRange.contains(v3));

        // Dual condition search: "Beta Small" must NOT be returned
        List<Venue> dualSearch = venueRepository.findLargeVenuesWithName(200, "Beta");
        assertEquals(1, dualSearch.size());
        assertEquals(v2, dualSearch.get(0));
        assertFalse(dualSearch.contains(betaSmall));
    }

    @Test
    @DisplayName("7. Event search methods: date range boundaries (min, max, min-1, max+1), title, and dual condition")
    void testEventSearchMethods() {
        LocalDate min = LocalDate.of(2026, 11, 10);
        LocalDate max = LocalDate.of(2026, 11, 20);

        Event beforeMin = new Event("E01", "Java Intro", v1, EventCategory.TECH, min.minusDays(1), min);
        Event onMin = new Event("E02", "Java Core", v1, EventCategory.TECH, min, min.plusDays(1));
        Event inside = new Event("E03", "Spring Boot", v2, EventCategory.SCIENCE, LocalDate.of(2026, 11, 15), LocalDate.of(2026, 11, 16));
        Event onMax = new Event("E04", "Java Cloud", v2, EventCategory.BUSINESS, max, max.plusDays(1));
        Event afterMax = new Event("E05", "Java Future", v3, EventCategory.TECH, max.plusDays(1), max.plusDays(2));

        eventRepository.addAll(List.of(beforeMin, onMin, inside, onMax, afterMax));

        // Boundary check: min and max included, min-1 and max+1 excluded
        List<Event> inRange = eventRepository.findByDateRange(min, max);
        assertEquals(3, inRange.size());
        assertTrue(inRange.containsAll(List.of(onMin, inside, onMax)));
        assertFalse(inRange.contains(beforeMin));
        assertFalse(inRange.contains(afterMax));

        // Title partial match search
        List<Event> byTitle = eventRepository.findByTitleContaining("Spring");
        assertEquals(1, byTitle.size());
        assertEquals(inside, byTitle.get(0));

        // Dual condition search: category TECH + date range [min, max] (excludes BUSINESS onMax and out-of-range TECH)
        List<Event> dualSearch = eventRepository.findByCategoryAndDateRange(EventCategory.TECH, min, max);
        assertEquals(1, dualSearch.size());
        assertEquals(onMin, dualSearch.get(0));
    }

    @Test
    @DisplayName("8. Search method returns Optional.empty() when no elements match")
    void testFindOneReturnsEmptyOptional() {
        Optional<Venue> result = venueRepository.findFirstWithCapacityAtLeast(1000);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("9. groupingBy and countByCategory return expected groups and counts")
    void testGroupingByAndCounting() {
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

        Map<EventCategory, Long> counts = eventRepository.countByCategory();
        assertEquals(2L, counts.get(EventCategory.TECH));
        assertEquals(1L, counts.get(EventCategory.SCIENCE));
    }

    @Test
    @DisplayName("10. Aggregation on empty repository does not throw exception and returns default")
    void testAggregationOnEmptyRepository() {
        VenueRepository emptyRepo = new VenueRepository();
        assertEquals(0.0, emptyRepo.getAverageCapacity(), 0.001);
    }

    @Test
    @DisplayName("11. Reusing consumed stream throws IllegalStateException")
    void testReusingStreamThrowsException() {
        var stream = venueRepository.stream();
        stream.toList();

        assertThrows(IllegalStateException.class, stream::toList);
    }
}