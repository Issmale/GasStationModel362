package org.example.fuelInventory;

import java.util.List;
import java.util.Map;

public class FuelInventorySystem implements FuelInventorySystemInterface {
    private final FilesFuel filesFuel;
    private String locationID;

    public FuelInventorySystem(FilesFuel filesFuel) {
        this.filesFuel = filesFuel;
    }

    public void checkLowInventoryAcrossLocations() {
        for (Map.Entry<String, List<FuelUpdate>> entry : filesFuel.getAllInventoryData().entrySet()) {
            locationID = entry.getKey();
            List<FuelUpdate> fuelUpdates = entry.getValue();
            alertLowInventory(fuelUpdates);
        }
    }

    @Override
    public boolean updateFuelInventory(String locationID, List<FuelUpdate> fuelUpdates) {
        List<FuelUpdate> currentInventory = filesFuel.getFuelInventoryData(locationID);
        if (currentInventory == null || currentInventory.isEmpty()) {
            System.out.println("Location not found or no existing data for location: " + locationID);
            return false;
        }

        for (FuelUpdate update : fuelUpdates) {
            boolean found = false;
            for (FuelUpdate existing : currentInventory) {
                if (existing.getFuelType().equalsIgnoreCase(update.getFuelType())) {
                    double newQuantity = existing.getQuantity() + update.getQuantity(); // Using quantity as quantity change
                    if (newQuantity < 0) {
                        warnInvalidUpdate(List.of(update)); // Warn about invalid update
                        return false;
                    }
                    existing.setQuantity(newQuantity);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Fuel type " + update.getFuelType() + " not found for location " + locationID);
                return false;
            }
        }
        filesFuel.putFuelInventoryData(locationID, currentInventory); // Save changes
        return true;
    }

    @Override
    public void alertLowInventory(List<FuelUpdate> fuelUpdates) {
        for (FuelUpdate fuel : fuelUpdates) {
            if (fuel.getQuantity() < fuel.getReorderLevel()) {
                System.out.println("Alert: Low inventory for " + fuel.getFuelType() +
                        " at quantity " + fuel.getQuantity() + " at location: " + locationID);
            }
        }
    }

    @Override
    public void warnInvalidUpdate(List<FuelUpdate> fuelUpdates) {
        for (FuelUpdate fuel : fuelUpdates) {
            System.out.println("Warning: Invalid update for " + fuel.getFuelType() +
                    " with attempted quantity " + fuel.getQuantity());
        }
    }
}
