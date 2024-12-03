package org.example.expiry;

import java.util.Date;

public interface StoreEmployee {
    boolean verifyExpiryDate(String itemID, Date newExpiryDate);
    boolean handleSystemError();
}

