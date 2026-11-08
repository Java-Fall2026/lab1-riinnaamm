package ua.conference.repository;

import ua.conference.model.Venue;
import java.util.List;
import java.util.Optional;

public class SearchBenchmark {

    public static void main(String[] args) {
        GenericRepository<Venue> repo = new GenericRepository<>("BenchmarkRepo", Venue::getVenueCode);

        int count = 100_000;
        for (int i = 0; i < count; i++) {
            repo.addIfAbsent(Venue.of("V-" + i, "Hall " + i, 100));
        }

        List<Venue> itemsList = repo.findAll();
        IdentityExtractor<Venue> extractor = Venue::getVenueCode;

        String firstId = "V-0";
        String lastId = "V-99999";
        String missingId = "V-MISSING";


        for (int i = 0; i < 1000; i++) {
            repo.findByIdentity(firstId);
            linearSearch(itemsList, firstId, extractor);
        }

        System.out.println("=== Benchmark Results (100,000 items) ===");


        measure("Map Search (First)", () -> {
            Optional<Venue> res = repo.findByIdentity(firstId);
        });
        measure("List Linear Search (First)", () -> {
            Optional<Venue> res = linearSearch(itemsList, firstId, extractor);
        });


        measure("Map Search (Last)", () -> {
            Optional<Venue> res = repo.findByIdentity(lastId);
        });
        measure("List Linear Search (Last)", () -> {
            Optional<Venue> res = linearSearch(itemsList, lastId, extractor);
        });


        measure("Map Search (Missing)", () -> {
            Optional<Venue> res = repo.findByIdentity(missingId);
        });
        measure("List Linear Search (Missing)", () -> {
            Optional<Venue> res = linearSearch(itemsList, missingId, extractor);
        });
    }

    private static <E> Optional<E> linearSearch(List<E> items, String identity, IdentityExtractor<E> extractor) {
        for (E item : items) {
            if (identity.equals(extractor.identityOf(item))) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    private static void measure(String label, Runnable action) {
        long start = System.nanoTime();
        action.run();
        long end = System.nanoTime();
        System.out.printf("%-30s : %,d ns%n", label, (end - start));
    }
}