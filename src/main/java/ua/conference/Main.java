package ua.conference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.conference.exception.DuplicateIdentityException;
import ua.conference.model.Event;
import ua.conference.model.EventCategory;
import ua.conference.model.Participant;
import ua.conference.model.Venue;
import ua.conference.repository.EventRepository;
import ua.conference.repository.GenericRepository;
import ua.conference.repository.SortOrder;
import ua.conference.repository.VenueRepository;
import ua.conference.util.VenueLoader;

import java.time.LocalDate;
import java.util.*;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println(" 1. Наповнення доменних репозиторіїв (VenueRepository та EventRepository) ");
        VenueRepository venueRepository = new VenueRepository();
        EventRepository eventRepository = new EventRepository();

        Venue v1 = Venue.of("HALL-101", "Main Center", 250);
        Venue v2 = Venue.of("HALL-102", "Tech Room", 100);
        Venue v3 = Venue.of("HALL-103", "Alpha Conference Hall", 300);

        venueRepository.add(v1);
        venueRepository.add(v2);
        venueRepository.add(v3);

        LocalDate today = LocalDate.now();
        Event e1 = new Event("CONF-2026", "Java Tech Summit", v1, EventCategory.TECH, today, today.plusDays(2));
        Event e2 = new Event("CONF-2027", "AI Innovations", v2, EventCategory.SCIENCE, today.plusDays(5), today.plusDays(7));
        Event e3 = new Event("CONF-2028", "Business Leader Forum", v3, EventCategory.BUSINESS, today.plusDays(10), today.plusDays(12));

        eventRepository.add(e1);
        eventRepository.add(e2);
        eventRepository.add(e3);

        try {
            venueRepository.add(Venue.of("HALL-101", "Duplicate Hall", 300));
        } catch (DuplicateIdentityException e) {
            log.error("Перехоплено очікуване виключення при додаванні дубліката: {}", e.getMessage());
        }

        System.out.println("\n 2. Сортування за природним порядком та багаторівневим компаратором ");
        System.out.println(" Природний порядок (Comparable за venueCode): ");
        List<Venue> naturalSorted = new ArrayList<>(venueRepository.findAll());
        Collections.sort(naturalSorted);
        naturalSorted.forEach(System.out::println);

        System.out.println("\n Багаторівневе сортування (місткість DESC, далі назва ASC): ");
        venueRepository.findAllSortedByCapacityDescThenName().forEach(System.out::println);

        System.out.println("\n 3. Сортування за ключем identity (SortOrder.ASC та SortOrder.DESC) ");
        System.out.println(" Сортування за ключем ASC: ");
        venueRepository.findAllSortedByIdentity(SortOrder.ASC).forEach(System.out::println);

        System.out.println("\n Сортування за ключем DESC: ");
        venueRepository.findAllSortedByIdentity(SortOrder.DESC).forEach(System.out::println);

        System.out.println("\n 4. Поведінка TreeSet із компаратором, неузгодженим з equals ");
        Comparator<Venue> capacityComparator = Comparator.comparing(Venue::getCapacity);
        Set<Venue> treeSet = new TreeSet<>(capacityComparator);

        Venue vA = Venue.of("CODE1", "Hall A", 200);
        Venue vB = Venue.of("CODE2", "Hall B", 200); // Одинакова місткість, але різні коди

        treeSet.add(vA);
        treeSet.add(vB);

        System.out.println("Результат equals між об'єктами: " + vA.equals(vB));
        System.out.println("Кількість елементів у TreeSet: " + treeSet.size());
        System.out.println("Елементи в TreeSet: " + treeSet);

        System.out.println("\n 5. Пошук через Stream API ");
        System.out.println(" Пошук за частковим збігом назви ('Tech'): ");
        venueRepository.findByNameContaining("Tech").forEach(System.out::println);

        System.out.println("\n Пошук за діапазоном місткості [100, 260]: ");
        venueRepository.findByCapacityRange(100, 260).forEach(System.out::println);

        System.out.println("\n Пошук за двома умовами : ");
        venueRepository.findLargeVenuesWithName(200, "Main").forEach(System.out::println);

        System.out.println("\n 6. Обробка відсутнього результату Optional ");
        System.out.print("Пошук локації з місткістю >= 500: ");
        Optional<Venue> missingVenue = venueRepository.findFirstWithCapacityAtLeast(500);

        String venueName = missingVenue.map(Venue::getName).orElse("Локацію не знайдено (дефолтне значення)");
        System.out.println("Результат map + orElse: " + venueName);

        missingVenue.ifPresentOrElse(
                v -> System.out.println("Знайдено: " + v),
                () -> System.out.println("Розпакування через ifPresentOrElse: об'єкт відсутній")
        );

        System.out.println("\n 7. Групування та агрегація ");
        System.out.println(" Групування подій за категоріями (Collectors.groupingBy): ");
        Map<EventCategory, List<Event>> grouped = eventRepository.groupByCategory();
        grouped.forEach((category, events) -> System.out.println(category + " -> " + events));

        System.out.println("\n Середня місткість локацій (Агрегація): ");
        System.out.println("Середня місткість: " + venueRepository.getAverageCapacity());

        System.out.println("\n 8. Спроба повторного використання стріму ");
        var stream = venueRepository.stream();
        List<Venue> consumedList = stream.toList();
        System.out.println("Стрім використано першого разу, елементів: " + consumedList.size());

        try {
            System.out.println("Спроба використати той самий стрім повторно...");
            stream.toList();
        } catch (IllegalStateException e) {
            log.error("Успішно перехоплено виключення при повторному використанні стріму: {}", e.getMessage());
            System.out.println("Перехоплено виключення: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        System.out.println("\n 9. Додаткова перевірка ЛР4 (Participant та CSV Loader) ");
        GenericRepository<Participant> participantRepository = new GenericRepository<>("ParticipantRepo", Participant::getEmail);
        participantRepository.add(Participant.of("john.doe@example.com", "John Doe", "Tech Corp"));
        System.out.println("Participant repository size: " + participantRepository.size());

        List<Venue> csvVenues = VenueLoader.loadVenues("data/venues.csv");
        for (Venue csvVenue : csvVenues) {
            venueRepository.addIfAbsent(csvVenue);
        }
        System.out.println("Всього локацій після завантаження з CSV: " + venueRepository.size());
    }
}