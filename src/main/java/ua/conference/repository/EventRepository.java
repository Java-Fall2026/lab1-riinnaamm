package ua.conference.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.conference.model.Event;
import ua.conference.model.EventCategory;
import ua.conference.model.Venue;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventRepository extends GenericRepository<Event> {
    private static final Logger log = LoggerFactory.getLogger(EventRepository.class);

    public EventRepository() {
        super("events", Event::eventCode);
    }

    public List<Event> findAllSortedByStartDate() {
        log.info("[events] Sorting events by startDate ASC");
        return findAllSorted(Comparator.comparing(Event::startDate));
    }

    public List<Event> findAllSortedByCategoryThenDateDesc() {
        log.info("[events] Sorting events by category then startDate DESC");
        Comparator<Event> comp = Comparator.comparing(Event::category, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Comparator.comparing(Event::startDate).reversed());
        return findAllSorted(comp);
    }

    public List<Event> findByTitleContaining(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            log.warn("[events] Search by title keyword skipped: keyword is blank");
            return List.of();
        }
        List<Event> result = stream()
                .filter(e -> e.title() != null && e.title().toLowerCase().contains(keyword.toLowerCase()))
                .toList();

        if (result.isEmpty()) {
            log.warn("[events] Search by title keyword '{}': no events found", keyword);
        } else {
            log.info("[events] Search by title keyword '{}': found {}", keyword, result.size());
        }
        return result;
    }

    public List<Event> findByDateRange(LocalDate from, LocalDate to) {
        List<Event> result = stream()
                .filter(e -> !e.startDate().isBefore(from) && !e.startDate().isAfter(to))
                .toList();

        if (result.isEmpty()) {
            log.warn("[events] Search by date range [{}, {}]: no events found", from, to);
        } else {
            log.info("[events] Search by date range [{}, {}]: found {}", from, to, result.size());
        }
        return result;
    }

    public List<Event> findByCategoryAndDateRange(EventCategory category, LocalDate from, LocalDate to) {
        List<Event> result = stream()
                .filter(e -> e.category() == category)
                .filter(e -> !e.startDate().isBefore(from) && !e.startDate().isAfter(to))
                .toList();

        if (result.isEmpty()) {
            log.warn("[events] Search by category {} and date range [{}, {}]: no events found", category, from, to);
        } else {
            log.info("[events] Search by category {} and date range [{}, {}]: found {}", category, from, to, result.size());
        }
        return result;
    }

    public Optional<Event> findFirstByCategory(EventCategory category) {
        Optional<Event> result = stream()
                .filter(e -> e.category() == category)
                .findFirst();

        if (result.isEmpty()) {
            log.warn("[events] Search first event by category {}: not found", category);
        } else {
            log.info("[events] Search first event by category {}: found '{}'", category, result.get().eventCode());
        }
        return result;
    }

    public List<Venue> findDistinctVenuesUsed() {
        List<Venue> venues = stream()
                .map(Event::venue)
                .distinct()
                .toList();
        log.info("[events] Found {} distinct venues used across events", venues.size());
        return venues;
    }

    public List<String> findUpcomingEventTitles(int limit) {
        List<String> titles = stream()
                .sorted(Comparator.comparing(Event::startDate))
                .map(Event::title)
                .limit(limit)
                .toList();
        log.info("[events] Retrieved top {} upcoming event titles", titles.size());
        return titles;
    }

    public Map<EventCategory, List<Event>> groupByCategory() {
        Map<EventCategory, List<Event>> grouped = stream()
                .collect(Collectors.groupingBy(Event::category));
        log.info("[events] Grouped events by category: {} groups", grouped.size());
        return grouped;
    }

    public Map<EventCategory, Long> countByCategory() {
        Map<EventCategory, Long> counts = stream()
                .collect(Collectors.groupingBy(Event::category, Collectors.counting()));
        log.info("[events] Counted events by category: {}", counts);
        return counts;
    }
}