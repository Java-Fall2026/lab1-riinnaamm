package ua.conference.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.conference.exception.DuplicateIdentityException;

import java.util.*;
import java.util.Comparator;
import java.util.Map;
import java.util.List;
import java.util.stream.Stream;

public class GenericRepository<T> {
    private static final Logger log = LoggerFactory.getLogger(GenericRepository.class);

    private final String name;
    private final Map<String, T> items = new LinkedHashMap<>();
    private final IdentityExtractor<T> identityExtractor;

    public GenericRepository(String name, IdentityExtractor<T> identityExtractor) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Repository name cannot be null or blank");
        }
        if (identityExtractor == null) {
            throw new IllegalArgumentException("IdentityExtractor cannot be null");
        }
        this.name = name;
        this.identityExtractor = identityExtractor;
    }

    public String getName() {
        return name;
    }

    public void add(T item) {
        if (item == null) {
            log.error("[{}] Attempted to add null item", name);
            throw new IllegalArgumentException("Item cannot be null");
        }

        String identity = identityExtractor.identityOf(item);
        if (identity == null || identity.isBlank()) {
            log.error("[{}] Extracted identity is null or blank for item: {}", name, item);
            throw new IllegalArgumentException("Identity cannot be null or blank");
        }

        if (items.containsKey(identity)) {
            log.error("[{}] Duplicate identity detected: '{}'", name, identity);
            throw new DuplicateIdentityException("Item with identity '" + identity + "' already exists in repository '" + name + "'");
        }

        items.put(identity, item);
        log.info("[{}] Successfully added item with identity '{}'", name, identity);
    }

    public boolean addIfAbsent(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        String identity = identityExtractor.identityOf(item);
        if (identity == null || identity.isBlank()) {
            throw new IllegalArgumentException("Identity cannot be null or blank");
        }

        if (items.containsKey(identity)) {
            log.warn("[{}] Item with identity '{}' already exists. Skipped.", name, identity);
            return false;
        }

        items.put(identity, item);
        log.info("[{}] Successfully added absent item with identity '{}'", name, identity);
        return true;
    }

    public void addAll(Collection<T> collection) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection cannot be null");
        }
        for (T item : collection) {
            add(item);
        }
    }

    public boolean removeByIdentity(String identity) {
        if (identity == null || identity.isBlank()) {
            log.warn("[{}] Cannot remove item with null or blank identity", name);
            return false;
        }

        if (items.remove(identity) != null) {
            log.info("[{}] Successfully removed item with identity '{}'", name, identity);
            return true;
        }
        log.warn("[{}] Failed to remove: identity '{}' not found", name, identity);
        return false;
    }

    public Optional<T> findByIdentity(String identity) {
        if (identity == null || identity.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(items.get(identity));
    }

    public List<T> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(items.values()));
    }

    public int size() {
        return items.size();
    }

    public List<T> findAllSorted(Comparator<T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        List<T> sortedList = items.values().stream()
                .sorted(comparator)
                .toList();
        log.info("[{}] Executed findAllSorted with comparator. Returned {} items.", name, sortedList.size());
        return sortedList;
    }

    public List<T> findAllSortedByIdentity(SortOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Sort order cannot be null");
        }

        var entryComparator = Map.Entry.<String, T>comparingByKey();
        if (order == SortOrder.DESC) {
            entryComparator = entryComparator.reversed();
        }

        List<T> sortedList = items.entrySet().stream()
                .sorted(entryComparator)
                .map(Map.Entry::getValue)
                .toList();

        log.info("[{}] Executed findAllSortedByIdentity ({}). Returned {} items.", name, order, sortedList.size());
        return sortedList;
    }

    public Stream<T> stream() {
        return items.values().stream();
    }
}