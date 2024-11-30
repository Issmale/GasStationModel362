package org.example.storeLayout;

import java.util.List;

public interface TrafficDataInterface {
    int getFootTrafficAtTime(String time);
    List<String> getPeakTrafficTimes();
}

