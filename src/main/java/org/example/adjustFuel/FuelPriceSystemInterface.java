// FuelPriceSystemInterface.java
package org.example.adjustFuel;

public interface FuelPriceSystemInterface {
    double getCurrentPrice(String fuelType);
    boolean updatePrice(String fuelType, double newPrice);
    void logPriceChange(FuelPriceUpdate fuelUpdate);
}
