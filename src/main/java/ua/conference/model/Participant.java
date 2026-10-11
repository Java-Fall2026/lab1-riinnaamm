package ua.conference.model;

import ua.common.BaseEntity;
import ua.conference.util.ConferenceUtils;
import java.util.Objects;

public class Participant extends BaseEntity {
    private String email;
    private String fullName;
    private String organization;

    private Participant(String email, String fullName, String organization) {
        super();
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
                ", createdAt=" + createdAt +
                '}';
    }
}