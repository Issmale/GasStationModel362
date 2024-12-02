package org.example.expiry;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConcreteExpiryProcessor implements ExpiryProcessor {
    private InventoryModule inventoryModule;

    public ConcreteExpiryProcessor(InventoryModule inventoryModule) {
        this.inventoryModule = inventoryModule;
    }

    @Override
    public List<Item> viewItemsWithExpiry() {
        return inventoryModule.getAllItems();
    }

    @Override
    public List<Item> sortItemsByExpiry() {
        return inventoryModule.getAllItems().stream()
                .sorted(Comparator.comparing(Item::getExpiryDate))
                .collect(Collectors.toList());
    }

    @Override
    public boolean verifyPhysicalStock(String itemID, int actualQuantity) {
        Item item = inventoryModule.getItemById(itemID);
        if (item != null) {
            return item.getQuantity() == actualQuantity;
        }
        return false;
    }
}
