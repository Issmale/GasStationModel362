package org.example.feedback;

public class NotificationServiceSystem implements NotificationService {

    @Override
    public boolean sendNotification(String customerID, String message) {
        System.out.println("Notification sent to Customer ID: " + customerID + " - Message: " + message);
        return true;
    }

    @Override
    public boolean sendAnonymousAcknowledgment(String feedbackID) {
        System.out.println("Anonymous acknowledgment sent for Feedback ID: " + feedbackID);
        return true;
    }
}
