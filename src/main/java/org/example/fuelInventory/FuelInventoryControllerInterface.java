package org.example.fuelInventory;

import java.util.List;

public interface FuelInventoryControllerInterface {
    // Manages inventory by either adding or retrieving data
    boolean manageFuelInventory(String command, String locationID, List<FuelUpdate> fuelUpdates);
}
