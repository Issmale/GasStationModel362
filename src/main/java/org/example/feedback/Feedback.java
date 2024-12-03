package org.example.feedback;

public class Feedback {
    private String feedbackID;
    private String details;
    private String category;
    private String status;
    private boolean isAnonymous;
    private CustomerInformation customerInfo;

    // Constructor for anonymous feedback
    public Feedback(String feedbackID, String details, boolean isAnonymous) {
        this.feedbackID = feedbackID;
        this.details = details;
        this.isAnonymous = isAnonymous;
        this.status = "New";
    }

    // Constructor for non-anonymous feedback
    public Feedback(String feedbackID, String details, boolean isAnonymous, CustomerInformation customerInfo) {
        this(feedbackID, details, isAnonymous);
        this.customerInfo = customerInfo;
    }

    // Getters
    public String getFeedbackID() {
        return feedbackID;
    }

    public String getDetails() {
        return details;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public CustomerInformation getCustomerInfo() {
        return customerInfo;
    }

    // Setters
    public void setFeedbackID(String feedbackID) {
        this.feedbackID = feedbackID;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public void setCustomerInfo(CustomerInformation customerInfo) {
        this.customerInfo = customerInfo;
    }
}
