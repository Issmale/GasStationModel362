package org.example.storeInventory;


import java.util.List;

public interface StoreInventorySystemInterface {
    boolean updateInventory(String locationID, List<StoreUpdate> productUpdates);
    void alertLowInventory(List<StoreUpdate> productUpdates);
    void warnInvalidUpdate(List<StoreUpdate> productUpdates);
}
