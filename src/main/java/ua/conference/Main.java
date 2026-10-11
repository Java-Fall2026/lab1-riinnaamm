package ua.conference;

import ua.conference.model.*;
import ua.conference.util.ConferenceUtils;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 1. Створення об'єктів конструктором та фабричним методом ===");
        Venue venue1 = Venue.of("  HALL-A ", " Main Hall ", 500);
        Participant participant1 = Participant.of(" User1@Mail.COM ", " Ivan Franko ", " LNU ");
        Event event1 = new Event("CONF-2026", "Java Conference", venue1, " tech ", LocalDate.of(2026, 10, 15), LocalDate.of(2026, 10, 17));
        EventRegistration reg1 = new EventRegistration(event1, participant1, " vip ", LocalDate.of(2026, 10, 10));

        System.out.println("=== 2. Демонстрація нормалізації ===");
        System.out.println("Email (вхід: ' User1@Mail.COM '): " + participant1.getEmail());
        System.out.println("Category (вхід: ' tech '): " + event1.getCategory());
        System.out.println("TicketType (вхід: ' vip '): " + reg1.getTicketType());

        System.out.println("\n=== 3. Порушення правил валідації (try/catch) ===");
        try {
            Venue.of("HALL-B", "Small Room", -5);
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка 1: " + e.getMessage());
        }

        try {
            new Event("CONF-ERR", "Invalid Category", venue1, "pizza", LocalDate.now(), LocalDate.now().plusDays(1));
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка 2: " + e.getMessage());
        }

        try {
            new EventRegistration(event1, participant1, "STANDARD", LocalDate.of(2026, 10, 20));
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка 3: " + e.getMessage());
        }

        System.out.println("\n=== 4. Спроба зіпсувати об'єкт сеттером ===");
        try {
            venue1.setCapacity(0);
        } catch (IllegalArgumentException e) {
            System.out.println("Сеттер відхилив некоректне значення: " + e.getMessage());
        }

        System.out.println("\n=== 5. Порівняння ==/equals/hashCode для однакових і різних об'єктів ===");
        Participant participant2 = Participant.of("user1@mail.com", "Ivan Franko (копія)", "Inша Організація");
        Participant participant3 = Participant.of("other@mail.com", "Petro Mohyla", "NaUKMA");

        System.out.println("participant1 == participant2: " + (participant1 == participant2));
        System.out.println("participant1.equals(participant2): " + participant1.equals(participant2));
        System.out.println("hashCode однакові: " + (participant1.hashCode() == participant2.hashCode()));
        System.out.println("participant1.equals(participant3): " + participant1.equals(participant3));

        System.out.println("\n=== 6. Виклик обчислюваних методів ===");
        System.out.println("Тривалість події (eventDays): " + ConferenceUtils.eventDays(event1) + " дн.");
        System.out.println("Днів до початку реєстрації (daysBeforeEvent): " + ConferenceUtils.daysBeforeEvent(reg1) + " дн.");

        System.out.println("\n=== 7. Вивід усіх сутностей через toString() ===");
        System.out.println(venue1);
        System.out.println(participant1);
        System.out.println(event1);
        System.out.println(reg1);

        // System.out.println(ua.conference.util.ValidationHelper.class.getName());
        // Рядок вище не скомпілюється, оскільки ValidationHelper є package-private класом у пакеті ua.conference.util і недоступний ззовні.
    }
}