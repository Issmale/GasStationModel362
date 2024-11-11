package org.example.itemDelivery;

import java.util.List;

public class InventoryController implements InventoryControllerInterface {
    private InventorySystem inventorySystem;

    public InventoryController() {
        inventorySystem = new InventorySystem();
    }

    @Override
    public boolean processDelivery(String supplierID, List<InventoryItem> items) {
        // Process each item and forward to the inventory system
        for (InventoryItem item : items) {
            if (!inventorySystem.updateInventory(item, item.getQuantity())) {
                System.out.println("Failed to update inventory for item: " + item.getItemID());
                return false;
            }
        }
        return true;
    }
}
