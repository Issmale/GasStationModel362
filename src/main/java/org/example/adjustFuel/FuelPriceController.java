package org.example.adjustFuel;

import java.util.ArrayList;
import java.util.List;

public class FuelPriceController implements FuelPriceControllerInterface {
    private FuelPriceSystem fuelPriceSystem;
    private List<FuelPump> pumps; // List of fuel pumps to update

    public FuelPriceController() {
        fuelPriceSystem = new FuelPriceSystem();
        pumps = new ArrayList<>(); // Initialize pump list
    }

    public void addFuelPump(FuelPump pump) {
        pumps.add(pump); // Add pumps when the system initializes
    }

    @Override
    public boolean adjustPrice(String fuelType, double newPrice) {
        if (fuelPriceSystem.updatePrice(fuelType, newPrice)) {
            // Update each pump's display after adjusting the price
            for (FuelPump pump : pumps) {
                pump.updateDisplay(fuelType, newPrice);
            }
            return true;
        }
        return false;
    }
}
