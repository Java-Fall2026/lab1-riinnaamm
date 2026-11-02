package ua.conference.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GenericRepository<T> {
    private static final Logger log = LoggerFactory.getLogger(GenericRepository.class);

    private final List<T> items = new ArrayList<>();
    private final IdentityExtractor<T> identityExtractor;

    public GenericRepository(IdentityExtractor<T> identityExtractor) {
        if (identityExtractor == null) {
            throw new IllegalArgumentException("IdentityExtractor cannot be null");
        }
        this.identityExtractor = identityExtractor;
    }

    public boolean add(T item) {
        if (item == null) {
            log.warn("Attempted to add null item to repository");
            return false;
        }

        String identity = identityExtractor.extractIdentity(item);
        if (findByIdentity(identity).isPresent()) {
            log.warn("Duplicate entry detected with identity '{}'. Item was not added.", identity);
            return false;
        }

        items.add(item);
        log.info("Successfully added item with identity '{}'", identity);
        return true;
    }

    public boolean removeByIdentity(String identity) {
        Optional<T> itemToRemove = findByIdentity(identity);
        if (itemToRemove.isPresent()) {
            items.remove(itemToRemove.get());
            log.info("Successfully removed item with identity '{}'", identity);
            return true;
        }
        log.warn("Failed to remove item: identity '{}' not found", identity);
        return false;
    }

    public Optional<T> findByIdentity(String identity) {
        if (identity == null || identity.isBlank()) {
            return Optional.empty();
        }
        return items.stream()
                .filter(item -> identity.equals(identityExtractor.extractIdentity(item)))
                .findFirst();
    }

    public List<T> findAll() {
        return Collections.unmodifiableList(items);
    }
}