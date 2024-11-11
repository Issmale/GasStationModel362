package org.example.storeInventory;


import java.util.List;

/**
 * @Author Issmale Bekri
 */

public interface StoreInventorySystemInterface {
    boolean updateInventory(String locationID, List<StoreUpdate> productUpdates);
    void alertLowInventory(List<StoreUpdate> productUpdates);
    void warnInvalidUpdate(List<StoreUpdate> productUpdates);
}
