package org.example.scheduleWorkforce;

import org.example.scheduleWorkforce.EmployeeAvailability;
import org.example.scheduleWorkforce.SchedulingSystemInterface;
import org.example.scheduleWorkforce.Shift;
import org.example.scheduleWorkforce.ShiftUpdate;

import java.util.List;

public class SchedulingSystem implements SchedulingSystemInterface{


    @Override
    public void alertInvalidEmployeeInfo(String storeID, String invalidEmployeeID) {

    }

    @Override
    public void alertScheduleConflict(String storeID, Shift conflictingShift) {

    }


    @Override
    public void alertInsufficientStaff(String storeID, int missingStaffCount) {

    }
}