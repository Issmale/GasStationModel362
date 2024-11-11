// FilesFuelInterface.java
package org.example.fuelInventory;

import java.util.List;

/**
 * @Author Issmale Bekri
 */

public interface FilesFuelInterface {
    boolean putFuelInventoryData(String locationID, List<FuelUpdate> fuelUpdates);
    List<FuelUpdate> getFuelInventoryData(String locationID);
}
