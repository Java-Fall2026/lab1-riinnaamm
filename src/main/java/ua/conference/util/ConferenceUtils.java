package ua.conference.util;

import ua.conference.model.Event;
import ua.conference.model.EventRegistration;
import java.time.temporal.ChronoUnit;

public class ConferenceUtils {
    // 1-й обчислюваний метод з профілю: тривалість події у днях
    public static long eventDays(Event event) {
        ValidationHelper.requireNonNull(event, "Event");
        return ChronoUnit.DAYS.between(event.getStartDate(), event.getEndDate()) + 1;
    }

    // 2-й обчислюваний метод з профілю: за скільки днів до початку зареєструвалися
    public static long daysBeforeEvent(EventRegistration reg) {
        ValidationHelper.requireNonNull(reg, "EventRegistration");
        return ChronoUnit.DAYS.between(reg.getRegistrationDate(), reg.getEvent().getStartDate());
    }
}