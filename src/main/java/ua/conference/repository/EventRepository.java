package ua.conference.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.conference.model.Event;
import ua.conference.model.EventCategory;

import java.time.LocalDate;
import java.util.*;
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

    public List<Event> findByDateRange(LocalDate from, LocalDate to) {
        List<Event> result = stream()
                .filter(e -> !e.startDate().isBefore(from) && !e.startDate().isAfter(to))
                .toList();
        log.info("[events] Search by date range [{}, {}]: found {}", from, to, result.size());
        return result;
    }

    public Map<EventCategory, List<Event>> groupByCategory() {
        return stream().collect(Collectors.groupingBy(Event::category));
    }


    public Map<EventCategory, Long> countByCategory() {
        return stream().collect(Collectors.groupingBy(Event::category, Collectors.counting()));
    }
}