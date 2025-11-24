package org.example;

public class PricingCalculator {

    public static final int REGULAR_PRICE = 200;
    public static final int WEEKEND_PRICE = 250;

    public double calculatePrice(boolean isWeekend, boolean isMember, boolean isGroup, int age, boolean isBefore7AM) {
        if (age < 3 || age > 75) {
            throw new IllegalArgumentException("Age must be between 3 and 75.");
        }

        double price = isWeekend ? WEEKEND_PRICE : REGULAR_PRICE;

        if (isMember) {
            price *= 0.5;
        } else if (isGroup) {
            price *= 0.7;
        } else if (age <= 12 || age >= 60) {
            price *= 0.8;
        } else if (isBefore7AM) {
            price *= 0.8;
        }

        return price;
    }
}
