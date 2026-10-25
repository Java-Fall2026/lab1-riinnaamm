package ua.conference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.conference.model.Venue;
import ua.conference.util.VenueLoader;

import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("=== 1. Читання CSV-файлу (коректні та некоректні рядки) ===");
        log.info("Starting CSV reading process...");
        List<Venue> venues = VenueLoader.loadVenues("data/venues.csv");
        System.out.println("Прочитані коректні об'єкти Venue:");
        venues.forEach(System.out::println);

        System.out.println("\n=== 2. Спроба прочитати відсутній файл ===");
        List<Venue> emptyList = VenueLoader.loadVenues("data/non_existent_file.csv");
        System.out.println("Кількість прочитаних об'єктів з відсутнього файлу: " + emptyList.size());

        System.out.println("\n=== 3. Некоректне значення в сеттері змінюваного класу ===");
        Venue venue = Venue.of("HALL-X", "Testing Room", 100);
        try {
            System.out.println("Спроба встановити від'ємну місткість (-10)...");
            venue.setCapacity(-10);
        } catch (IllegalArgumentException e) {
            log.error("Caught validation error when calling setter: {}", e.getMessage());
            System.out.println("Перехоплено виключення сеттера: " + e.getMessage());
        }

        System.out.println("\n=== 4. Демонстрація логування трьох рівнів ===");
        log.info("Повідомлення рівня INFO: Операцію завершено успішно.");
        log.warn("Повідомлення рівня WARN: Зверніть увагу на можливі ризики.");
        log.error("Повідомлення рівня ERROR: Виникла помилка в системі.");
    }
}