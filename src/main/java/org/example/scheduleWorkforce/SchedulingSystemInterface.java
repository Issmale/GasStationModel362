package org.example.scheduleWorkforce;

import org.example.scheduleWorkforce.EmployeeAvailability;

import java.util.List;

public interface SchedulingSystemInterface {
    void alertInvalidEmployeeInfo(String storeID, String invalidEmployeeID);
    void alertScheduleConflict(String storeID, Shift conflictingShift);
    void alertInsufficientStaff(String storeID, int missingStaffCount);
}

