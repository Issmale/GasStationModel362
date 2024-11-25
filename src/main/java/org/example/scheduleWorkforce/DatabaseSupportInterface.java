package org.example.scheduleWorkforce;

import java.util.List;

public interface DatabaseSupportInterface {
    boolean putWorkScheduleData(String storeID, List<ShiftUpdate> scheduleUpdates);
    List<EmployeeAvailability> getEmployeeAvailability(String storeID);
    List<EmployeeInfo> fetchEmployeeDetails(String storeID);
    boolean removeEmployeeFromSchedule(String storeID, String employeeID, String dayToRemove);
}

