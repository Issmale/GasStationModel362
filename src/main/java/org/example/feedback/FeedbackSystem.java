package org.example.feedback;

import java.io.*;
import java.util.Scanner;

public class FeedbackSystem {
    private final FeedbackService feedbackService;
    private final ResolutionService resolutionService;
    private static final String RESOLUTION_FILE = "resolution_logs.csv";

    public FeedbackSystem() {
        this.feedbackService = new FeedbackServiceSystem();
        this.resolutionService = new ResolutionServiceSystem();
        initializeResolutionFile();
    }

    public void execute() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Feedback Management System");
            System.out.println("1. Log Feedback");
            System.out.println("2. Categorize Feedback");
            System.out.println("3. Retrieve Feedback");
            System.out.println("4. Update Feedback Status");
            System.out.println("5. Resolve Feedback");
            System.out.println("6. Show Resolution Logs");
            System.out.println("7. Exit Feedback System");
            System.out.print("Please select an option (1-7): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    logFeedback(scanner);
                    break;
                case 2:
                    categorizeFeedback(scanner);
                    break;
                case 3:
                    retrieveFeedback(scanner);
                    break;
                case 4:
                    updateFeedbackStatus(scanner);
                    break;
                case 5:
                    resolveFeedback(scanner);
                    break;
                case 6:
                    showResolutionLogs();
                    break;
                case 7:
                    exit = true;
                    System.out.println("Exiting Feedback System.");
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 7.");
            }
            System.out.println(); // Add an empty line for readability
        }
        scanner.close();
    }

    private void logFeedback(Scanner scanner) {
        System.out.print("Enter Feedback ID: ");
        String feedbackID = scanner.nextLine();

        if (feedbackService.retrieveFeedback(feedbackID) != null) {
            System.out.println("Feedback ID already exists. Please use a unique ID.");
            return;
        }

        System.out.print("Enter Feedback Details: ");
        String details = scanner.nextLine();
        System.out.print("Is this feedback anonymous? (true/false): ");
        boolean isAnonymous = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline

        Feedback feedback;
        if (!isAnonymous) {
            System.out.print("Enter Customer ID: ");
            String customerID = scanner.nextLine();
            System.out.print("Enter Customer Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Customer Email: ");
            String email = scanner.nextLine();

            CustomerInformation customerInfo = new CustomerInformation(customerID, name, email);
            feedback = new Feedback(feedbackID, details, false, customerInfo);
        } else {
            feedback = new Feedback(feedbackID, details, true);
        }

        feedbackService.logFeedback(feedback);
        System.out.println("Feedback logged successfully.");
    }

    private void categorizeFeedback(Scanner scanner) {
        System.out.print("Enter Feedback ID to categorize: ");
        String feedbackID = scanner.nextLine();

        if (feedbackService.retrieveFeedback(feedbackID) == null) {
            System.out.println("Feedback ID not found.");
            return;
        }

        System.out.print("Enter Category: ");
        String category = scanner.nextLine();
        feedbackService.categorizeFeedback(feedbackID, category);
        System.out.println("Feedback categorized successfully.");
    }

    private void retrieveFeedback(Scanner scanner) {
        System.out.print("Enter Feedback ID to retrieve: ");
        String feedbackID = scanner.nextLine();

        Feedback feedback = feedbackService.retrieveFeedback(feedbackID);
        if (feedback == null) {
            System.out.println("Feedback ID not found.");
            return;
        }

        System.out.println("Feedback Details: " + feedback.getDetails() +
                ", Category: " + feedback.getCategory() +
                ", Status: " + feedback.getStatus() +
                ", Anonymous: " + feedback.isAnonymous());
        if (!feedback.isAnonymous() && feedback.getCustomerInfo() != null) {
            System.out.println("Customer ID: " + feedback.getCustomerInfo().getCustomerID());
            System.out.println("Customer Name: " + feedback.getCustomerInfo().getName());
            System.out.println("Customer Email: " + feedback.getCustomerInfo().getEmail());
        }
    }

    private void updateFeedbackStatus(Scanner scanner) {
        System.out.print("Enter Feedback ID to update status: ");
        String feedbackID = scanner.nextLine();

        if (feedbackService.retrieveFeedback(feedbackID) == null) {
            System.out.println("Feedback ID not found.");
            return;
        }

        System.out.print("Enter New Status: ");
        String status = scanner.nextLine();
        feedbackService.updateFeedbackStatus(feedbackID, status);
        System.out.println("Feedback status updated successfully.");
    }

    private void resolveFeedback(Scanner scanner) {
        System.out.print("Enter Feedback ID to resolve: ");
        String feedbackID = scanner.nextLine();

        if (feedbackService.retrieveFeedback(feedbackID) == null) {
            System.out.println("Feedback ID not found.");
            return;
        }

        System.out.print("Enter Resolution ID: ");
        String resolutionID = scanner.nextLine();
        System.out.print("Enter Resolution Details: ");
        String details = scanner.nextLine();
        String timestamp = java.time.LocalDate.now().toString();

        Resolution resolution = new Resolution(resolutionID, feedbackID, details, timestamp);
        resolutionService.logResolution(feedbackID, resolution);
        logResolutionToCSV(resolution);
        System.out.println("Feedback resolved successfully.");
    }

    private void showResolutionLogs() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RESOLUTION_FILE))) {
            String line;
            System.out.println("Resolution Logs:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading resolution logs.");
            e.printStackTrace();
        }
    }

    private void logResolutionToCSV(Resolution resolution) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESOLUTION_FILE, true))) {
            writer.printf("%s,%s,%s,%s%n",
                    resolution.getResolutionID(),
                    resolution.getFeedbackID(),
                    resolution.getResolutionDetails(),
                    resolution.getTimestamp());
        } catch (IOException e) {
            System.out.println("Error logging resolution.");
            e.printStackTrace();
        }
    }

    private void initializeResolutionFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESOLUTION_FILE, true))) {
            File file = new File(RESOLUTION_FILE);
            if (file.length() == 0) {
                writer.println("ResolutionID,FeedbackID,ResolutionDetails,Timestamp");
            }
        } catch (IOException e) {
            System.out.println("Error initializing resolution file.");
            e.printStackTrace();
        }
    }
}
