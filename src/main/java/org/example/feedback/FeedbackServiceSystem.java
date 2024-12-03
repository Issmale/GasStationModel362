package org.example.feedback;

import java.io.*;
import java.util.*;

public class FeedbackServiceSystem implements FeedbackService {
    private static final String FEEDBACK_FILE = "feedback_data.csv";

    public FeedbackServiceSystem() {
        // Initialize the CSV file if it doesn't exist
        File file = new File(FEEDBACK_FILE);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("FeedbackID,Details,Category,Status,IsAnonymous");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean logFeedback(Feedback feedback) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FEEDBACK_FILE, true))) {
            writer.printf("%s,%s,%s,%s,%b%n",
                    feedback.getFeedbackID(),
                    feedback.getDetails(),
                    feedback.getCategory() == null ? "" : feedback.getCategory(),
                    feedback.getStatus(),
                    feedback.isAnonymous());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Feedback retrieveFeedback(String feedbackID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FEEDBACK_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // Keep empty fields
                if (parts[0].equals(feedbackID)) {
                    Feedback feedback = new Feedback(parts[0], parts[1], Boolean.parseBoolean(parts[4]));
                    feedback.setCategory(parts[2].isEmpty() ? null : parts[2]);
                    feedback.setStatus(parts[3]);
                    return feedback;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean categorizeFeedback(String feedbackID, String category) {
        List<String[]> allData = readAllFeedback();
        boolean updated = false;

        try (PrintWriter writer = new PrintWriter(new FileWriter(FEEDBACK_FILE))) {
            writer.println("FeedbackID,Details,Category,Status,IsAnonymous");
            for (String[] parts : allData) {
                if (parts[0].equals(feedbackID)) {
                    parts[2] = category;
                    updated = true;
                }
                writer.printf("%s,%s,%s,%s,%s%n", (Object[]) parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return updated;
    }

    @Override
    public boolean updateFeedbackStatus(String feedbackID, String status) {
        List<String[]> allData = readAllFeedback();
        boolean updated = false;

        try (PrintWriter writer = new PrintWriter(new FileWriter(FEEDBACK_FILE))) {
            writer.println("FeedbackID,Details,Category,Status,IsAnonymous");
            for (String[] parts : allData) {
                if (parts[0].equals(feedbackID)) {
                    parts[3] = status;
                    updated = true;
                }
                writer.printf("%s,%s,%s,%s,%s%n", (Object[]) parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return updated;
    }

    private List<String[]> readAllFeedback() {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FEEDBACK_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                data.add(line.split(",", -1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
