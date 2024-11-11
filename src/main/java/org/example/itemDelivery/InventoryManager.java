package org.example.itemDelivery;

import java.util.List;

public class InventoryManager implements InventoryManagerInterface {
    private InventoryController controller;

    public InventoryManager() {
        controller = new InventoryController();
    }

    @Override
    public boolean receiveGoods(String supplierID, List<InventoryItem> items) {
        return controller.processDelivery(supplierID, items);
    }
}
