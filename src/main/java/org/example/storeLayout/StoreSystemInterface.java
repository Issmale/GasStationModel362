package org.example.storeLayout;

public interface StoreSystemInterface {
    StoreLayout fetchStoreLayoutData(String storeID);
    SalesData fetchSalesAndTrafficData(String storeID);
    boolean applyLayoutChanges(String storeID, StoreLayout adjustedLayout);
    void alertInsufficientData(String storeID);
    void alertLayoutConflict(String storeID);
    void alertStaffResistance(String storeID);
    boolean saveNewLayout(String storeID, StoreLayout adjustedLayout);
}

