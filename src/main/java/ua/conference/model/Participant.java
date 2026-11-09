package ua.conference.model;

import ua.conference.util.ConferenceUtils;
import java.util.Objects;

public class Participant implements Comparable<Participant> {
    private String email;
    private String fullName;
    private String organization;

    private Participant(String email, String fullName, String organization) {
        setEmail(email);
        setFullName(fullName);
        setOrganization(organization);
    }

    public static Participant of(String email, String fullName, String organization) {
        return new Participant(email, fullName, organization);
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        ConferenceUtils.validateNonNullOrEmpty(email, "Email");
        this.email = ConferenceUtils.normalizeEmail(email);
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) {
        ConferenceUtils.validateNonNullOrEmpty(fullName, "FullName");
        this.fullName = ConferenceUtils.normalizeString(fullName);
    }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) {
        ConferenceUtils.validateNonNullOrEmpty(organization, "Organization");
        this.organization = ConferenceUtils.normalizeString(organization);
    }

    @Override
    public int compareTo(Participant other) {
        if (other == null) {
            return 1;
        }
        int emailComparison = this.email.compareTo(other.email);
        if (emailComparison != 0) {
            return emailComparison;
        }
        return this.fullName.compareTo(other.fullName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", organization='" + organization + '\'' +
                '}';
    }
}