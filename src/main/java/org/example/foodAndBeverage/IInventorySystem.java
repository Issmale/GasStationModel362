package org.example.foodAndBeverage;

import java.util.List;

public interface IInventorySystem {
    List<Item> retrieveInventory();
    List<Item> reviewAndSelectItems(List<Item> inventory);
    void calculateCostAndProfit(List<Item> items);
    void finalizeMenu(List<Item> items);
}
