package org.example.measureTanks;

public class FuelTankRecord {
    private String tankId;
    private double fuelLevel;

    public FuelTankRecord(String tankId, double fuelLevel) {
        this.tankId = tankId;
        this.fuelLevel = fuelLevel;
    }

    public String getTankId() {
        return tankId;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    @Override
    public String toString() {
        return "TankID: " + tankId + ", Fuel Level: " + fuelLevel;
    }
}
