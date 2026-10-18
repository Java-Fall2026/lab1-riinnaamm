package ua.conference.model;

import ua.conference.util.ConferenceUtils;
import java.time.LocalDate;
import java.util.Objects;

public record EventRegistration(
     Event event,
     Participant participant,
     TicketType ticketType,
     LocalDate registrationDate
) {


    public EventRegistration {
        ConferenceUtils.validateNonNull(event, "Event");
        ConferenceUtils.validateNonNull(participant, "Participant");
        ConferenceUtils.validateNonNull(registrationDate, "RegistrationDate");


        if (registrationDate.isAfter(event.startDate())) {
            throw new IllegalArgumentException("RegistrationDate expected <= StartDate (" + event.startDate() + "), but got: " + registrationDate);
        }
    }
}