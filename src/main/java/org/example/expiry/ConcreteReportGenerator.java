package org.example.expiry;

import java.util.List;

public class ConcreteReportGenerator implements ReportGenerator {
    private InventoryModule inventoryModule;

    public ConcreteReportGenerator(InventoryModule inventoryModule) {
        this.inventoryModule = inventoryModule;
    }

    @Override
    public String generateExpiryReport() {
        List<Item> items = inventoryModule.getAllItems();
        StringBuilder report = new StringBuilder("Expiry Report:\n");
        for (Item item : items) {
            report.append(String.format("Item: %s, Expiry Date: %s, Days Until Expiry: %d\n",
                    item.getName(), item.getExpiryDate(), item.daysUntilExpiry()));
        }
        return report.toString();
    }
}
