package ua.conference;

import ua.conference.model.*;
import ua.conference.util.ConferenceUtils;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Успішний сценарій ===");
        try {
            Participant p = Participant.of("john@example.com", "John Doe", "TechCorp");
            Venue v = Venue.of("HALL-A", "Main Hall", 500);
            Event e = new Event("E001", "Java Conf 2026", v, "TECH",
                    LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 12));
            EventRegistration reg = new EventRegistration(e, p, "VIP", LocalDate.of(2026, 10, 1));

            System.out.println("Сутності успішно створено:");
            System.out.println(p);
            System.out.println(v);
            System.out.println(e);
            System.out.println(reg);

            System.out.println("\nОбчислювані методи:");
            System.out.println("Тривалість події (днів): " + ConferenceUtils.eventDays(e));
            System.out.println("Зареєструвалися за (днів до початку): " + ConferenceUtils.daysBeforeEvent(reg));
        } catch (Exception ex) {
            System.out.println("Помилка: " + ex.getMessage());
        }

        System.out.println("\n=== Невдалий сценарій ===");

        // 1. Некоректний Email (не в нижньому регістрі)
        try {
            Participant.of("INVALID_EMAIL@Test.com", "Jane Doe", "Org");
        } catch (IllegalArgumentException ex) {
            System.out.println("Виловлено помилку [Email]: " + ex.getMessage());
        }

        // 2. Capacity <= 0
        try {
            Venue.of("HALL-B", "Small Room", -5);
        } catch (IllegalArgumentException ex) {
            System.out.println("Виловлено помилку [Capacity]: " + ex.getMessage());
        }

        // 3. EndDate < StartDate
        try {
            Venue v = Venue.of("HALL-A", "Main Hall", 500);
            new Event("E002", "Bad Event", v, "TECH",
                    LocalDate.of(2026, 10, 12), LocalDate.of(2026, 10, 10));
        } catch (IllegalArgumentException ex) {
            System.out.println("Виловлено помилку [Event Dates]: " + ex.getMessage());
        }

        // 4. Реєстрація після початку події
        try {
            Participant p = Participant.of("kate@example.com", "Kate", "Org");
            Venue v = Venue.of("HALL-A", "Main Hall", 500);
            Event e = new Event("E001", "Java Conf", v, "TECH",
                    LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 12));
            new EventRegistration(e, p, "STANDARD", LocalDate.of(2026, 10, 15));
        } catch (IllegalArgumentException ex) {
            System.out.println("Виловлено помилку [Registration Date]: " + ex.getMessage());
        }
    }
}