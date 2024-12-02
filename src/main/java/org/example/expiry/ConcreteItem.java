package org.example.expiry;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ConcreteItem implements Item {
    private String itemID;
    private String name;
    private Date expiryDate;
    private int quantity;

    public ConcreteItem(String itemID, String name, Date expiryDate, int quantity) {
        this.itemID = itemID;
        this.name = name;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
    }

    @Override
    public String getItemID() {
        return itemID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Date getExpiryDate() {
        return expiryDate;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean isExpired() {
        return new Date().after(expiryDate);
    }

    @Override
    public int daysUntilExpiry() {
        long diffInMillies = expiryDate.getTime() - new Date().getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
