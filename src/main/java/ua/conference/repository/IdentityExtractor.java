package ua.conference.repository;

@FunctionalInterface
public interface IdentityExtractor<T> {
    String identityOf(T item);
}