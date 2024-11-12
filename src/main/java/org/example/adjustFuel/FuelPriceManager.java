// FuelPriceManager.java
package org.example.adjustFuel;

public class FuelPriceManager implements FuelPriceManagerInterface {
    private FuelPriceController controller;

    public FuelPriceManager() {
        this.controller = new FuelPriceController();
    }

    @Override
    public boolean adjustFuelPrice(String fuelType, double newPrice) {
        return controller.adjustFuelPrice(fuelType, newPrice);
    }
}
