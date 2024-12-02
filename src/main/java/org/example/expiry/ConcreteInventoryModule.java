package org.example.expiry;

import java.util.ArrayList;
import java.util.List;

public class ConcreteInventoryModule implements InventoryModule {
    private List<Item> items;

    public ConcreteInventoryModule() {
        this.items = new ArrayList<>();
    }

    @Override
    public boolean accessInventory() {
        return true;
    }

    @Override
    public boolean updateInventory(Item item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemID().equals(item.getItemID())) {
                items.set(i, item);
                return true;
            }
        }
        items.add(item);
        return true;
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    @Override
    public Item getItemById(String itemID) {
        for (Item item : items) {
            if (item.getItemID().equals(itemID)) {
                return item;
            }
        }
        return null;
    }
}
