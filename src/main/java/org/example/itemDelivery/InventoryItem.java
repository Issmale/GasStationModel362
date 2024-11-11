package org.example.itemDelivery;

public class InventoryItem implements InventoryItemInterface {
    private String itemID;
    private String description;
    private int quantity;
    private double unitPrice;

    public InventoryItem(String itemID, String description, int quantity, double unitPrice) {
        this.itemID = itemID;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    @Override
    public String getItemID() {
        return itemID;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public double getUnitPrice() {
        return unitPrice;
    }
}

