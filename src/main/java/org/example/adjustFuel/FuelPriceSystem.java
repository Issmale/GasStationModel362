// FuelPriceSystem.java
package org.example.adjustFuel;

import java.util.HashMap;
import java.util.Map;

public class FuelPriceSystem implements FuelPriceSystemInterface {
    private Map<String, Double> priceHistory;

    public FuelPriceSystem() {
        this.priceHistory = new HashMap<>();
    }

    @Override
    public double getCurrentPrice(String fuelType) {
        return priceHistory.getOrDefault(fuelType, 0.0);
    }

    @Override
    public boolean updatePrice(String fuelType, double newPrice) {
        double oldPrice = getCurrentPrice(fuelType);
        priceHistory.put(fuelType, newPrice);
        logPriceChange(new FuelPriceUpdate(fuelType, oldPrice, newPrice));
        return true;
    }

    @Override
    public void logPriceChange(FuelPriceUpdate fuelUpdate) {
        System.out.println("Logged Price Change: " + fuelUpdate);
    }
}
