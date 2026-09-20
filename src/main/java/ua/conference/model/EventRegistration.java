package ua.conference.model;

import ua.common.BaseEntity;
import ua.conference.util.ValidationHelper;
import java.time.LocalDate;
import java.util.Objects;

public class EventRegistration extends BaseEntity {
    private Event event;
    private Participant participant;
    private String ticketType;
    private LocalDate registrationDate;

    public EventRegistration(Event event, Participant participant, String ticketType, LocalDate registrationDate) {
        super();
        ValidationHelper.requireNonNull(event, "Event");
        ValidationHelper.requireNonNull(participant, "Participant");
        ValidationHelper.requireNonNullOrEmpty(ticketType, "TicketType");
        ValidationHelper.requireNonNull(registrationDate, "RegistrationDate");

        if (registrationDate.isAfter(event.getStartDate())) {
            throw new IllegalArgumentException("RegistrationDate expected <= StartDate (" + event.getStartDate() + "), but got: " + registrationDate);
        }

        this.event = event;
        this.participant = participant;
        this.ticketType = ticketType;
        this.registrationDate = registrationDate;
    }

    public Event getEvent() { return event; }
    public Participant getParticipant() { return participant; }
    public String getTicketType() { return ticketType; }
    public LocalDate getRegistrationDate() { return registrationDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventRegistration that = (EventRegistration) o;
        return Objects.equals(event, that.event) && Objects.equals(participant, that.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, participant);
    }

    @Override
    public String toString() {
        return "EventRegistration{" + "ticketType='" + ticketType + '\'' + ", registrationDate=" + registrationDate + '}';
    }
}