package org.example.scheduleWorkforce;

import java.util.List;

public class ScheduleController implements ScheduleControllerInterface{

    @Override
    public boolean checkScheduleConflicts(String storeID, List<ShiftUpdate> scheduleUpdates) {
        return false;
    }

    @Override
    public boolean resolveScheduleConflict(String storeID, Shift shift) {
        return false;
    }

}
