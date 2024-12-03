package org.example.scheduleWorkforce;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulingSystem implements SchedulingSystemInterface {

    private final DatabaseSupport databaseSupport;

    public SchedulingSystem(DatabaseSupport databaseSupport) {
        this.databaseSupport = databaseSupport;
    }

    @Override
    public void alertInsufficientStaff(String storeID) {
        // Fetch the schedule data for the store
        List<ShiftUpdate> shiftUpdates = databaseSupport.getWorkScheduleData(storeID);

        // Use a map to store the last shift for each day
        Map<String, ShiftUpdate> lastShiftOfDay = new HashMap<>();

        // Iterate over shifts and keep track of the last instance for each day
        for (ShiftUpdate shift : shiftUpdates) {
            lastShiftOfDay.put(shift.getDay(), shift);
        }

        // Alert if any day is understaffed in the last instance
        System.out.println("Understaffed Shifts:");
        boolean anyUnderstaffed = false;

        for (ShiftUpdate shift : lastShiftOfDay.values()) {
            if (shift.getAvailableStaff() < shift.getRequiredStaff()) {
                anyUnderstaffed = true;
                int shortage = shift.getRequiredStaff() - shift.getAvailableStaff();
                System.out.println("Day: " + shift.getDay() +
                        ", Shift: " + shift.getShiftTime() +
                        ", Required Staff: " + shift.getRequiredStaff() +
                        ", Available Staff: " + shift.getAvailableStaff() +
                        ", Shortage: " + shortage);
            }
        }

        if (!anyUnderstaffed) {
            System.out.println("No understaffed shifts found.");
        }
    }
}
