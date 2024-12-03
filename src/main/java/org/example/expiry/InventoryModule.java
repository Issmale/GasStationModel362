package org.example.expiry;

import java.util.List;

public interface InventoryModule {
    boolean accessInventory();
    boolean updateInventory(Item item);
    List<Item> getAllItems();
    Item getItemById(String itemID);
}
