package org.example.feedback;

public class Resolution {
    private String resolutionID;
    private String feedbackID;
    private String resolutionDetails;
    private String timestamp;

    // Constructor
    public Resolution(String resolutionID, String feedbackID, String resolutionDetails, String timestamp) {
        this.resolutionID = resolutionID;
        this.feedbackID = feedbackID;
        this.resolutionDetails = resolutionDetails;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getResolutionID() {
        return resolutionID;
    }

    public String getFeedbackID() {
        return feedbackID;
    }

    public String getResolutionDetails() {
        return resolutionDetails;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
