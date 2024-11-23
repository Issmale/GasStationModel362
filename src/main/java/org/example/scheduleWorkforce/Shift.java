package org.example.scheduleWorkforce;

import java.util.List;

public class Shift implements ShiftInterface{
    @Override
    public String getShiftID() {
        return "";
    }

    @Override
    public String getEmployeeID() {
        return "";
    }

    @Override
    public String getShiftTime() {
        return "";
    }

    @Override
    public boolean isConflictWithOtherShifts(List<Shift> otherShifts) {
        return false;
    }

    @Override
    public void setShiftDetails(String employeeID, String shiftTime) {

    }
}
