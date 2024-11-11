// FilesFuelInterface.java
package org.example.fuelInventory;

import java.util.List;

public interface FilesFuelInterface {
    boolean putFuelInventoryData(String locationID, List<FuelUpdate> fuelUpdates);
    List<FuelUpdate> getFuelInventoryData(String locationID);
}
