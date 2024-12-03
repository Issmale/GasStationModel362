package org.example.expiry;

import java.util.Date;

public interface Item {
    String getItemID();
    String getName();
    Date getExpiryDate();
    int getQuantity();
    void setQuantity(int quantity);
    boolean isExpired();
    int daysUntilExpiry();
}
