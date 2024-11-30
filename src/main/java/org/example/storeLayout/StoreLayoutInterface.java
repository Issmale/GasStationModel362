package org.example.storeLayout;

public interface StoreLayoutInterface {
    boolean updateLayout(String layoutID, StoreLayout updatedLayout);
    boolean addShelf(String shelfID, String side, String product, int spaceAvailable);
    String getSideForProduct(String product);
    boolean isProductInStore(String product);
    void saveLayout(String csvFilePath);
    Shelf getShelfByID(String shelfID);
    void displayLayout();
    void loadLayout(String csvFilePath);
}

