package org.example.itemDelivery;

import java.util.List;

public interface InventorySystemInterface {
    List<InventoryItem> getExpectedItems(String supplierID);
    boolean updateInventory(InventoryItem item, int quantity);
    void recordInventoryUpdate(InventoryUpdate inventoryUpdate);
}
