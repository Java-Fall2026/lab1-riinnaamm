package ua.conference;

import ua.conference.model.*;
import ua.conference.util.ConferenceUtils;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 1. Створення record-об'єкта та звернення до його компонентів ===");
        Venue venue1 = Venue.of("HALL-A", "Main Hall", 500);
        Event event1 = new Event("CONF-2026", "Java Conference", venue1, EventCategory.TECH, LocalDate.of(2026, 10, 15), LocalDate.of(2026, 10, 17));

        System.out.println("Код події через рекордер (event1.eventCode()): " + event1.eventCode());
        System.out.println("Категорія (event1.category()): " + event1.category());

        System.out.println("\n=== 2. Enum у роботі ===");
        System.out.println("Перебір усіх значений EventCategory (values()):");
        for (EventCategory cat : EventCategory.values()) {
            System.out.println(" - " + cat);
        }

        System.out.println("Доступ до власного поля TicketType.VIP (priceMultiplier): " + TicketType.VIP.getPriceMultiplier());

        try {
            System.out.println("Парсинг неіснуючого enum значення 'INVALID'...");
            EventCategory.valueOf("INVALID");
        } catch (IllegalArgumentException e) {
            System.out.println("Перехоплено виняток для некоректного enum: " + e.getMessage());
        }

        System.out.println("\n=== 3. Обидва switch-вирази ===");
        System.out.println("Опис категорії (switch №1): " + ConferenceUtils.getCategoryDescription(event1.category()));
        System.out.println("Ціна квитка VIP при базовій 1000 грн (switch №2): " + ConferenceUtils.calculateTicketPrice(TicketType.VIP, 1000.0) + " грн.");

        System.out.println("\n=== 4. Record із невалідними даними (компактний конструктор) ===");
        try {
            new Event("CONF-ERR", "Invalid Event", venue1, EventCategory.TECH, LocalDate.of(2026, 10, 20), LocalDate.of(2026, 10, 15));
        } catch (IllegalArgumentException e) {
            System.out.println("Отримано очікуваний IllegalArgumentException: " + e.getMessage());
        }

        System.out.println("\n=== 5. Порівняння двох однакових record (equals та hashCode) ===");
        Participant participant1 = Participant.of("user@mail.com", "Ivan Franko", "LNU");
        EventRegistration reg1 = new EventRegistration(event1, participant1, TicketType.STANDARD, LocalDate.of(2026, 10, 10));
        EventRegistration reg2 = new EventRegistration(event1, participant1, TicketType.STANDARD, LocalDate.of(2026, 10, 10));

        System.out.println("reg1.equals(reg2): " + reg1.equals(reg2));
        System.out.println("reg1.hashCode() == reg2.hashCode(): " + (reg1.hashCode() == reg2.hashCode()));

        System.out.println("\n=== 6. Вивід record через згенерований toString() ===");
        System.out.println("Згенерований toString() для Event: " + event1);
        System.out.println("Згенерований toString() для EventRegistration: " + reg1);
    }
}