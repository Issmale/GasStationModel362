package org.example.storeLayout;

public class StoreSystem implements StoreSystemInterface{
    @Override
    public StoreLayout fetchStoreLayoutData(String storeID) {
        return null;
    }

    @Override
    public SalesData fetchSalesAndTrafficData(String storeID) {
        return null;
    }

    @Override
    public boolean applyLayoutChanges(String storeID, StoreLayout adjustedLayout) {
        return false;
    }


    @Override
    public void alertInsufficientData(String storeID) {

    }

    @Override
    public void alertLayoutConflict(String storeID) {

    }

    @Override
    public void alertStaffResistance(String storeID) {

    }

    @Override
    public boolean saveNewLayout(String storeID, StoreLayout adjustedLayout) {
        return false;
    }
}
