package org.example.itemDelivery;

import java.util.List;

public class SupplierTruck implements SupplierTruckInterface {
    @Override
    public void unloadGoods(List<InventoryItem> items) {
        System.out.println("Unloading goods from supplier truck...");
        for (InventoryItem item : items) {
            System.out.println("Unloaded item: " + item.getDescription() + " - Quantity: " + item.getQuantity());
        }
    }
}
