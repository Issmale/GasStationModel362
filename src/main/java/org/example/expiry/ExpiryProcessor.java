package org.example.expiry;

import java.util.List;

public interface ExpiryProcessor {
    List<Item> viewItemsWithExpiry();
    List<Item> sortItemsByExpiry();
    boolean verifyPhysicalStock(String itemID, int actualQuantity);
}
