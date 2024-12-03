package org.example.expiry;

import java.util.Date;

public class ConcreteStoreEmployee implements StoreEmployee {
    private InventoryModule inventoryModule;

    public ConcreteStoreEmployee(InventoryModule inventoryModule) {
        this.inventoryModule = inventoryModule;
    }

    @Override
    public boolean verifyExpiryDate(String itemID, Date newExpiryDate) {
        Item item = inventoryModule.getItemById(itemID);
        if (item != null) {
            return newExpiryDate.after(new Date());
        }
        return false;
    }

    @Override
    public boolean handleSystemError() {
        System.out.println("System error handled by employee");
        return true;
    }
}
