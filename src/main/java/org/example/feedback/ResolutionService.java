package org.example.feedback;

public interface ResolutionService {
    boolean logResolution(String feedbackID, Resolution resolution);
    Resolution retrieveResolution(String feedbackID);
    boolean updateResolutionDetails(String resolutionID, Resolution newDetails);
}
