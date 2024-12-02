package org.example.Coupon;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CouponHandlingDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Double> inventory = initializeInventory();
        ConcreteCouponValidator couponValidator = new ConcreteCouponValidator();
        ConcreteTransactionManager transactionManager = new ConcreteTransactionManager(0);
        ConcreteDiscountApplier discountApplier = new ConcreteDiscountApplier(couponValidator, transactionManager);
        SalesRecordManager salesRecordManager = new ConcreteSalesRecordManager();
        ReceiptPrinter receiptPrinter = new ConcreteReceiptPrinter();
        POSSystem posSystem = new ConcretePOSSystem(couponValidator, discountApplier, transactionManager, salesRecordManager, receiptPrinter);
        Cashier cashier = new ConcreteCashier(posSystem);

        while (true) {
            Map<String, Integer> cart = new HashMap<>();
            transactionManager.setCurrentTotal(0);

            while (true) {
                displayMenu();
                int choice = getUserChoice(scanner);
                switch (choice) {
                    case 1:
                        displayInventory(inventory);
                        break;
                    case 2:
                        addItemToCart(scanner, inventory, cart);
                        break;
                    case 3:
                        displayCart(cart, inventory, transactionManager);
                        break;
                    case 4:
                        updateTransactionTotal(cart, inventory, transactionManager);
                        applyCoupon(scanner, cashier, transactionManager);
                        break;
                    case 5:
                        if (checkout(cart, inventory, posSystem, transactionManager)) {
                            cart.clear();
                            transactionManager.setCurrentTotal(0);
                        }
                        break;
                    case 6:
                        System.out.println("Thank you for shopping with us!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    private static Map<String, Double> initializeInventory() {
        Map<String, Double> inventory = new HashMap<>();
        inventory.put("Apple", 0.50);
        inventory.put("Banana", 0.30);
        inventory.put("Orange", 0.75);
        inventory.put("Milk", 2.50);
        inventory.put("Bread", 1.50);
        return inventory;
    }

    private static void displayInventory(Map<String, Double> inventory) {
        System.out.println("\nInventory:");
        for (Map.Entry<String, Double> entry : inventory.entrySet()) {
            System.out.printf("%s: $%.2f%n", entry.getKey(), entry.getValue());
        }
    }

    private static void addItemToCart(Scanner scanner, Map<String, Double> inventory, Map<String, Integer> cart) {
        System.out.print("Enter item name: ");
        String item = scanner.nextLine();
        if (!inventory.containsKey(item)) {
            System.out.println("Item not found in inventory.");
            return;
        }
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        cart.put(item, cart.getOrDefault(item, 0) + quantity);
        System.out.println("Item added to cart.");
    }

    private static void displayCart(Map<String, Integer> cart, Map<String, Double> inventory, TransactionManager transactionManager) {
        System.out.println("\nCart:");
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String item = entry.getKey();
            int quantity = entry.getValue();
            double price = inventory.get(item);
            double itemTotal = price * quantity;
            System.out.printf("%s x%d: $%.2f%n", item, quantity, itemTotal);
        }
        System.out.printf("Total: $%.2f%n", transactionManager.getCurrentTotal());
    }

    private static void updateTransactionTotal(Map<String, Integer> cart, Map<String, Double> inventory, TransactionManager transactionManager) {
        double total = calculateTotal(cart, inventory);
        transactionManager.updateTotal(total);
    }

    private static void applyCoupon(Scanner scanner, Cashier cashier, TransactionManager transactionManager) {
        System.out.print("Enter coupon code: ");
        String couponCode = scanner.nextLine();
        if (cashier.enterCouponCode(couponCode)) {
            System.out.println("Coupon applied successfully.");
            try {
                double discountAmount = ((ConcreteDiscountApplier) ((ConcretePOSSystem) ((ConcreteCashier) cashier).getPosSystem()).getDiscountApplier()).applyDiscount(couponCode);
                // Apply the discount to the current total
                if (transactionManager.applyDiscount(discountAmount)) {
                    System.out.printf("New total after discount: $%.2f%n", transactionManager.getCurrentTotal());
                }
            } catch (Exception e) {
                System.out.println("Error applying discount: " + e.getMessage());
            }
        } else {
            System.out.println("Failed to apply coupon.");
        }
    }

    private static boolean checkout(Map<String, Integer> cart, Map<String, Double> inventory, POSSystem posSystem, TransactionManager transactionManager) {
        double currentTotal = transactionManager.getCurrentTotal();
        if (posSystem.finalizeSale(currentTotal)) {
            System.out.println("Transaction completed successfully.");
            return true;
        } else {
            System.out.println("Transaction failed.");
            return false;
        }
    }

    private static double calculateTotal(Map<String, Integer> cart, Map<String, Double> inventory) {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String item = entry.getKey();
            int quantity = entry.getValue();
            double price = inventory.get(item);
            total += price * quantity;
        }
        return total;
    }

    private static void displayMenu() {
        System.out.println("\n1. View inventory");
        System.out.println("2. Add item to cart");
        System.out.println("3. View cart");
        System.out.println("4. Apply coupon");
        System.out.println("5. Checkout");
        System.out.println("6. Exit");
    }

    private static int getUserChoice(Scanner scanner) {
        System.out.print("Enter your choice: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
}