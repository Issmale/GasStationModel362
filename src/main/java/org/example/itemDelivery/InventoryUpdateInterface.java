package org.example.itemDelivery;

import java.util.Date;

public interface InventoryUpdateInterface {
    String getItemID();
    int getQuantityReceived();
    int getQuantityExpected();
    Date getTimestamp();
}
