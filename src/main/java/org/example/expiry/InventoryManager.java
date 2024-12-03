package org.example.expiry;

public interface InventoryManager {
    boolean removeExpiredItems();
    boolean updateAfterRemoval(String itemID);
}
