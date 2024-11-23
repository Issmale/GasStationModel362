package org.example.scheduleWorkforce;

import org.example.scheduleWorkforce.EmployeeAvailability;
import org.example.scheduleWorkforce.SchedulingSystemInterface;
import org.example.scheduleWorkforce.Shift;
import org.example.scheduleWorkforce.ShiftUpdate;

import java.util.List;

public class SchedulingSystem implements SchedulingSystemInterface {
    @Override
    public List<EmployeeAvailability> fetchEmployeeAvailability(String storeID) {
        return List.of();
    }

    @Override
    public boolean applyScheduleUpdates(String storeID, List<ShiftUpdate> scheduleUpdates) {
        return false;
    }

    @Override
    public boolean validateEmployeeInfo(String storeID, List<ShiftUpdate> scheduleUpdates) {
        return false;
    }

    @Override
    public void alertInvalidEmployeeInfo(String storeID, String invalidEmployeeID) {

    }

    @Override
    public boolean validateScheduleConflict(String storeID, List<ShiftUpdate> scheduleUpdates) {
        return false;
    }

    @Override
    public void alertScheduleConflict(String storeID, Shift conflictingShift) {

    }

    @Override
    public boolean validateStaffingLevels(String storeID, List<ShiftUpdate> scheduleUpdates) {
        return false;
    }

    @Override
    public void alertInsufficientStaff(String storeID, int missingStaffCount) {

    }
}
