package org.example.adjustFuel;

public class FuelPriceManager implements FuelPriceManagerInterface {
    private FuelPriceController controller;

    public FuelPriceManager() {
        controller = new FuelPriceController();
    }

    @Override
    public boolean adjustFuelPrice(String fuelType, double newPrice) {
        return controller.adjustPrice(fuelType, newPrice);
    }
}
