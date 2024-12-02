package org.example.expiry;

import java.util.List;
import java.util.stream.Collectors;

public class ConcreteAlertSystem implements AlertSystem {
    private InventoryModule inventoryModule;

    public ConcreteAlertSystem(InventoryModule inventoryModule) {
        this.inventoryModule = inventoryModule;
    }

    @Override
    public List<Item> alertForExpiringItems(int daysThreshold) {
        return inventoryModule.getAllItems().stream()
                .filter(item -> item.daysUntilExpiry() <= daysThreshold)
                .collect(Collectors.toList());
    }
}
