// FuelInventorySystemInterface.java
package org.example.fuelInventory;

import java.util.List;

/**
 * @Author Issmale Bekri
 */

public interface FuelInventorySystemInterface {

    boolean updateFuelInventory(String locationID, List<FuelUpdate> fuelUpdates);
    void alertLowInventory(List<FuelUpdate> fuelUpdates);
    void warnInvalidUpdate(List<FuelUpdate> fuelUpdates);
}
