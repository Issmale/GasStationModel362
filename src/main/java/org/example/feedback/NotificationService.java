package org.example.feedback;

public interface NotificationService {
    boolean sendNotification(String customerID, String message);
    boolean sendAnonymousAcknowledgment(String feedbackID);
}
