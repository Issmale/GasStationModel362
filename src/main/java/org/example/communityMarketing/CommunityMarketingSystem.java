package org.example.communityMarketing;

import java.io.*;
import java.util.*;





interface ICommunityEventSystem {
    List<Event> suggestEvents(List<Customer> customers);
    List<Event> selectEvents(List<Event> events);  // Reverted to original signature
}

class CommunityEventSystem implements ICommunityEventSystem {
    private String eventsFile;

    public CommunityEventSystem(String eventsFile) {
        this.eventsFile = eventsFile;
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(eventsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    events.add(new Event(data[0], data[1], data[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading events: " + e.getMessage());
        }
        return events;
    }

    @Override
    public List<Event> suggestEvents(List<Customer> customers) {
        List<Event> allEvents = getAllEvents();
        List<Event> suggestedEvents = new ArrayList<>();

        Map<String, Integer> interestCounts = new HashMap<>();
        for (Customer customer : customers) {
            interestCounts.put(customer.getInterest(),
                    interestCounts.getOrDefault(customer.getInterest(), 0) + 1);
        }

        for (Event event : allEvents) {
            if (interestCounts.containsKey(event.getTheme())) {
                suggestedEvents.add(event);
            }
        }

        return suggestedEvents;
    }

    @Override
    public List<Event> selectEvents(List<Event> suggestedEvents) {
        Scanner scanner = new Scanner(System.in);
        List<Event> selectedEvents = new ArrayList<>();

        if (suggestedEvents.isEmpty()) {
            System.out.println("No suggested events available.");
            return selectedEvents;
        }

        System.out.println("Suggested Events:");
        for (int i = 0; i < suggestedEvents.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, suggestedEvents.get(i));
        }

        System.out.println("Enter the numbers of events you want to select (comma-separated):");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("No events selected.");
            return selectedEvents;
        }

        String[] selections = input.split(",");
        for (String selection : selections) {
            try {
                int index = Integer.parseInt(selection.trim()) - 1;
                if (index >= 0 && index < suggestedEvents.size()) {
                    selectedEvents.add(suggestedEvents.get(index));
                } else {
                    System.out.printf("Invalid event number: %s%n", selection);
                }
            } catch (NumberFormatException e) {
                System.out.printf("Invalid input: %s%n", selection);
            }
        }

        return selectedEvents;
    }

    public void addEvent(String name, String theme, String location) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(eventsFile, true))) {
            bw.write(String.format("%s,%s,%s%n", name, theme, location));
            System.out.println("Marketing added successfully.");
        } catch (IOException e) {
            System.out.println("Error adding event: " + e.getMessage());
        }
    }
}

class MarketingManager implements IMarketingManager {
    private ICustomerDataSystem customerDataSystem;
    private ICommunityEventSystem communityEventSystem;
    private IMarketingMaterialSystem marketingMaterialSystem;

    public MarketingManager(
            ICustomerDataSystem customerDataSystem,
            ICommunityEventSystem communityEventSystem
    ) {
        this.customerDataSystem = customerDataSystem;
        this.communityEventSystem = communityEventSystem;
        this.marketingMaterialSystem = new MarketingMaterialSystem("marketing_materials.csv");
    }

    @Override
    public void runCommunityCampaign() {
        // Retrieve customer data
        List<Customer> customers = customerDataSystem.retrieveCustomerData();
        if (customers.isEmpty()) {
            System.out.println("No customer data available. Please add customers first.");
            return;
        }

        // Suggest events
        List<Event> suggestedEvents = communityEventSystem.suggestEvents(customers);
        if (suggestedEvents.isEmpty()) {
            System.out.println("No suggested events found. Please add events that match customer interests.");
            return;
        }

        // Allow user to select events
        List<Event> selectedEvents = communityEventSystem.selectEvents(suggestedEvents);

        if (selectedEvents.isEmpty()) {
            System.out.println("No events selected. Campaign aborted.");
            return;
        }

        // Generate marketing materials
        marketingMaterialSystem.generateMarketingMaterials(selectedEvents);

        System.out.println("\nCommunity-based campaign successfully launched!");
    }
}






public class CommunityMarketingSystem {
    public static void main(String[] args) {
        CustomerDataSystem customerDataSystem = new CustomerDataSystem("customer_data.csv");
        CommunityEventSystem communityEventSystem = new CommunityEventSystem("community_events.csv");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Community Marketing System ---");
            System.out.println("1. Add Marketing Customer");
            System.out.println("2. Add Marketing Event");
            System.out.println("3. View Events");
            System.out.println("4. Run Community Campaign");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    addCustomer(scanner, customerDataSystem);
                    break;

                case 2:
                    addEvent(scanner, communityEventSystem);
                    break;

                case 3:
                    viewEvents(communityEventSystem);
                    break;

                case 4:
                    runCommunityCampaign(customerDataSystem, communityEventSystem);
                    break;

                case 5:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addCustomer(Scanner scanner, CustomerDataSystem customerDataSystem) {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter customer location: ");
        String location = scanner.nextLine().trim();
        System.out.print("Enter customer interest: ");
        String interest = scanner.nextLine().trim();

        if (name.isEmpty() || location.isEmpty() || interest.isEmpty()) {
            System.out.println("All fields are required. org.example.communityMarketing.Customer not added.");
        } else {
            customerDataSystem.addCustomer(name, location, interest);
        }
    }

    private static void addEvent(Scanner scanner, CommunityEventSystem communityEventSystem) {
        System.out.print("Enter event name: ");
        String eventName = scanner.nextLine().trim();
        System.out.print("Enter event theme: ");
        String theme = scanner.nextLine().trim();
        System.out.print("Enter event location: ");
        String eventLocation = scanner.nextLine().trim();

        if (eventName.isEmpty() || theme.isEmpty() || eventLocation.isEmpty()) {
            System.out.println("All fields are required. org.example.communityMarketing.Event not added.");
        } else {
            communityEventSystem.addEvent(eventName, theme, eventLocation);
        }
    }

    private static void viewEvents(CommunityEventSystem communityEventSystem) {
        List<Event> events = communityEventSystem.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events found.");
        } else {
            System.out.println("Available Events:");
            for (int i = 0; i < events.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, events.get(i));
            }
        }
    }

    private static void runCommunityCampaign(
            CustomerDataSystem customerDataSystem,
            CommunityEventSystem communityEventSystem
    ) {
        MarketingManager marketingManager = new MarketingManager(
                customerDataSystem,
                communityEventSystem
        );
        marketingManager.runCommunityCampaign();
    }
}


class Customer {
    private String name;
    private String location;
    private String interest;

    public Customer(String name, String location, String interest) {
        this.name = name;
        this.location = location;
        this.interest = interest;
    }

    public String getInterest() {
        return interest;
    }

    @Override
    public String toString() {
        return String.format("org.example.communityMarketing.Customer[name=%s, location=%s, interest=%s]", name, location, interest);
    }
}
class CustomerDataSystem implements ICustomerDataSystem {
    private String customerDataFile;

    public CustomerDataSystem(String customerDataFile) {
        this.customerDataFile = customerDataFile;
    }

    @Override
    public List<Customer> retrieveCustomerData() {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(customerDataFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                customers.add(new Customer(data[0], data[1], data[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public void addCustomer(String name, String location, String interest) {
        if (name.isEmpty() || location.isEmpty() || interest.isEmpty()) {
            System.out.println("org.example.communityMarketing.Customer name, location, and interest cannot be empty.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(customerDataFile, true))) {
            bw.write(String.format("%s,%s,%s%n", name, location, interest));
            System.out.println("org.example.communityMarketing.Customer added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Event {
    private String name;
    private String theme;
    private String location;

    public Event(String name, String theme, String location) {
        this.name = name;
        this.theme = theme;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getTheme() {
        return theme;
    }

    @Override
    public String toString() {
        return String.format("org.example.communityMarketing.Event[name=%s, theme=%s, location=%s]", name, theme, location);
    }
}

interface ICustomerDataSystem {
    List<Customer> retrieveCustomerData();
}
interface IMarketingMaterialSystem {
    void generateMarketingMaterials(List<Event> events);
}
interface IMarketingManager {
    void runCommunityCampaign();
}
class MarketingMaterialSystem implements IMarketingMaterialSystem {
    private String marketingMaterialFile;

    public MarketingMaterialSystem(String marketingMaterialFile) {
        this.marketingMaterialFile = marketingMaterialFile;
    }

    @Override
    public void generateMarketingMaterials(List<Event> events) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(marketingMaterialFile, true))) {
            for (Event event : events) {
                bw.write(String.format("Generated material for event: %s, Theme: %s\n", event.getName(), event.getTheme()));
            }
            System.out.println("Marketing materials generated and saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}









