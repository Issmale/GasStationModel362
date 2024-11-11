package org.example.adjustFuel;

public class FuelPump implements FuelPumpInterface {
    private double totalFuelDispensed;

    public FuelPump() {
        this.totalFuelDispensed = 0;
    }

    @Override
    public void updateDisplay(String fuelType, double newPrice) {
        System.out.println("Updated display at fuel pump: " + fuelType + " - $" + newPrice);
    }

    public void dispenseFuel(double amount) {
        totalFuelDispensed += amount;
        System.out.println("Dispensed " + amount + " liters. Total dispensed: " + totalFuelDispensed + " liters.");
    }

    public double getTotalFuelDispensed() {
        return totalFuelDispensed;
    }
}
