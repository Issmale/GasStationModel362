package org.example.feedback;

public interface FeedbackService {
    boolean logFeedback(Feedback feedback);
    Feedback retrieveFeedback(String feedbackID);
    boolean categorizeFeedback(String feedbackID, String category);
    boolean updateFeedbackStatus(String feedbackID, String status);
}
