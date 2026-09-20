package ua.conference.model;

import ua.common.BaseEntity;
import ua.conference.util.ValidationHelper;
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
        ValidationHelper.requireNonNullOrEmpty(email, "Email");
        ValidationHelper.requireLowerCase(email, "Email");
        this.email = email;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) {
        ValidationHelper.requireNonNullOrEmpty(fullName, "FullName");
        this.fullName = fullName;
    }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) {
        ValidationHelper.requireNonNullOrEmpty(organization, "Organization");
        this.organization = organization;
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
        return "Participant{" + "email='" + email + '\'' + ", fullName='" + fullName + '\'' + '}';
    }
}