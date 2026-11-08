package ua.conference.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.conference.exception.DuplicateIdentityException;

import java.util.*;

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

        String identity = identityExtractor.extractIdentity(item);
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

        String identity = identityExtractor.extractIdentity(item);
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
}