package ua.conference.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.conference.model.Venue;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class VenueRepository extends GenericRepository<Venue> {
    private static final Logger log = LoggerFactory.getLogger(VenueRepository.class);

    public VenueRepository() {
        super("venues", Venue::getVenueCode);
    }

    public List<Venue> findAllSortedByName() {
        log.info("[venues] Sorting venues by name ASC");
        return findAllSorted(Comparator.comparing(Venue::getName, Comparator.nullsLast(String::compareTo)));
    }

    public List<Venue> findAllSortedByCapacityDescThenName() {
        log.info("[venues] Sorting venues by capacity DESC then name ASC");
        Comparator<Venue> comp = Comparator.comparing(Venue::getCapacity).reversed()
                .thenComparing(Venue::getName);
        return findAllSorted(comp);
    }

    public List<Venue> findByNameContaining(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            log.warn("[venues] Search by name keyword skipped: keyword is blank");
            return List.of();
        }
        List<Venue> result = stream()
                .filter(v -> v.getName() != null && v.getName().toLowerCase().contains(keyword.toLowerCase()))
                .toList();

        if (result.isEmpty()) {
            log.warn("[venues] Search by name keyword '{}': no venues found", keyword);
        } else {
            log.info("[venues] Search by name keyword '{}': found {}", keyword, result.size());
        }
        return result;
    }

    public List<Venue> findByCapacityRange(int minCapacity, int maxCapacity) {
        List<Venue> result = stream()
                .filter(v -> v.getCapacity() >= minCapacity && v.getCapacity() <= maxCapacity)
                .sorted(Comparator.comparing(Venue::getCapacity))
                .toList();

        if (result.isEmpty()) {
            log.warn("[venues] Search by capacity range [{}, {}]: no venues found", minCapacity, maxCapacity);
        } else {
            log.info("[venues] Search by capacity range [{}, {}]: found {}", minCapacity, maxCapacity, result.size());
        }
        return result;
    }

    public List<Venue> findLargeVenuesWithName(int minCapacity, String namePart) {
        List<Venue> result = stream()
                .filter(v -> v.getCapacity() >= minCapacity)
                .filter(v -> v.getName() != null && v.getName().contains(namePart))
                .toList();

        if (result.isEmpty()) {
            log.warn("[venues] Search large venues (cap >= {}) with name part '{}': no venues found", minCapacity, namePart);
        } else {
            log.info("[venues] Search large venues (cap >= {}) with name part '{}': found {}", minCapacity, namePart, result.size());
        }
        return result;
    }

    public Optional<Venue> findFirstWithCapacityAtLeast(int minCapacity) {
        Optional<Venue> result = stream()
                .filter(v -> v.getCapacity() >= minCapacity)
                .findFirst();

        if (result.isEmpty()) {
            log.warn("[venues] Search first venue with capacity >= {}: not found", minCapacity);
        } else {
            log.info("[venues] Search first venue with capacity >= {}: found '{}'", minCapacity, result.get().getVenueCode());
        }
        return result;
    }

    public double getAverageCapacity() {
        double avg = stream()
                .mapToInt(Venue::getCapacity)
                .average()
                .orElse(0.0);

        if (size() == 0) {
            log.warn("[venues] Calculated average capacity on empty repository: 0.0");
        } else {
            log.info("[venues] Calculated average capacity across {} venues: {}", size(), avg);
        }
        return avg;
    }
}