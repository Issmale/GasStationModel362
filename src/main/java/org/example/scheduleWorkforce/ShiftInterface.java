package org.example.scheduleWorkforce;

import java.util.List;

public interface ShiftInterface {
    String getShiftID();
    String getEmployeeID();
    String getShiftTime();
    boolean isConflictWithOtherShifts(List<Shift> otherShifts);
    void setShiftDetails(String employeeID, String shiftTime);
}

