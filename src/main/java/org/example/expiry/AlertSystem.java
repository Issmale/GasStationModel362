package org.example.expiry;

import java.util.List;

public interface AlertSystem {
    List<Item> alertForExpiringItems(int daysThreshold);
}
