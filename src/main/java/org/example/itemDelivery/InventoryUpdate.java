package org.example.itemDelivery;

import java.util.Date;

public class InventoryUpdate implements InventoryUpdateInterface {
    private String itemID;
    private int quantityReceived;
    private int quantityExpected;
    private Date timestamp;

    public InventoryUpdate(String itemID, int quantityReceived, int quantityExpected) {
        this.itemID = itemID;
        this.quantityReceived = quantityReceived;
        this.quantityExpected = quantityExpected;
        this.timestamp = new Date();
    }

    @Override
    public String getItemID() {
        return itemID;
    }

    @Override
    public int getQuantityReceived() {
        return quantityReceived;
    }

    @Override
    public int getQuantityExpected() {
        return quantityExpected;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }
}

