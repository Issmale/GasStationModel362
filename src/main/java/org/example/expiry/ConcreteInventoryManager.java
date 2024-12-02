package org.example.expiry;

import java.util.List;
import java.util.stream.Collectors;

public class ConcreteInventoryManager implements InventoryManager {
    private InventoryModule inventoryModule;

    public ConcreteInventoryManager(InventoryModule inventoryModule) {
        this.inventoryModule = inventoryModule;
    }

    @Override
    public boolean removeExpiredItems() {
        List<Item> expiredItems = inventoryModule.getAllItems().stream()
                .filter(Item::isExpired)
                .collect(Collectors.toList());
        for (Item item : expiredItems) {
            updateAfterRemoval(item.getItemID());
        }
        return true;
    }

    @Override
    public boolean updateAfterRemoval(String itemID) {
        Item item = inventoryModule.getItemById(itemID);
        if (item != null) {
            item.setQuantity(0);
            return inventoryModule.updateInventory(item);
        }
        return false;
    }
}
