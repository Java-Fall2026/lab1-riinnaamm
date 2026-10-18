package ua.conference.model;

public enum TicketType {
    STANDARD(1.0),
    VIP(1.5),
    SPEAKER(0.0),
    PRESS(0.5);

    private final double priceMultiplier;

    TicketType(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }
}