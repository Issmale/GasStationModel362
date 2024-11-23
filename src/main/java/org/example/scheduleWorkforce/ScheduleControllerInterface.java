package org.example.scheduleWorkforce;

import java.util.List;

public interface ScheduleControllerInterface {
    boolean scheduleWorkforce(String storeID, List<ShiftUpdate> scheduleUpdates);
    boolean checkScheduleConflicts(String storeID, List<ShiftUpdate> scheduleUpdates);
    boolean resolveScheduleConflict(String storeID, Shift shift);
    boolean validateStaffingLevels(String storeID, List<ShiftUpdate> scheduleUpdates);
}

