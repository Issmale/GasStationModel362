package org.example.scheduleWorkforce;

import org.example.scheduleWorkforce.EmployeeAvailability;

import java.util.List;

public interface SchedulingSystemInterface {
    List<EmployeeAvailability> fetchEmployeeAvailability(String storeID);
    boolean applyScheduleUpdates(String storeID, List<ShiftUpdate> scheduleUpdates);
    boolean validateEmployeeInfo(String storeID, List<ShiftUpdate> scheduleUpdates);
    void alertInvalidEmployeeInfo(String storeID, String invalidEmployeeID);
    boolean validateScheduleConflict(String storeID, List<ShiftUpdate> scheduleUpdates);
    void alertScheduleConflict(String storeID, Shift conflictingShift);
    boolean validateStaffingLevels(String storeID, List<ShiftUpdate> scheduleUpdates);
    void alertInsufficientStaff(String storeID, int missingStaffCount);
}

