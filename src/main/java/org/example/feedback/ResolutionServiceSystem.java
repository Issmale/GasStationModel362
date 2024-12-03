package org.example.feedback;

import org.example.feedback.Resolution;
import org.example.feedback.Resolution;

import java.util.HashMap;
import java.util.Map;

public class ResolutionServiceSystem implements ResolutionService {
    private Map<String, Resolution> resolutionDatabase = new HashMap<>();

    @Override
    public boolean logResolution(String feedbackID, Resolution resolution) {
        resolutionDatabase.put(resolution.getResolutionID(), resolution);
        return true;
    }

    @Override
    public Resolution retrieveResolution(String feedbackID) {
        for (Resolution resolution : resolutionDatabase.values()) {
            if (resolution.getFeedbackID().equals(feedbackID)) {
                return resolution;
            }
        }
        return null;
    }

    @Override
    public boolean updateResolutionDetails(String resolutionID, Resolution newDetails) {
        if (resolutionDatabase.containsKey(resolutionID)) {
            resolutionDatabase.put(resolutionID, newDetails);
            return true;
        }
        return false;
    }
}
