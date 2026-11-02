package ua.conference.repository;

@FunctionalInterface
public interface IdentityExtractor<T> {
    String extractIdentity(T item);
}