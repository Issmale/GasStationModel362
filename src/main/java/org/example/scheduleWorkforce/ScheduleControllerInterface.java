package org.example.scheduleWorkforce;

import java.util.List;

public interface ScheduleControllerInterface {
    boolean checkScheduleConflicts(String storeID, List<ShiftUpdate> scheduleUpdates);
    boolean resolveScheduleConflict(String storeID, Shift shift);
}

