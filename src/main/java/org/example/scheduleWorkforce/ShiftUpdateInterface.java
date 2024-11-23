package org.example.scheduleWorkforce;

public interface ShiftUpdateInterface {
    boolean updateShift(String storeID, Shift shift);
    boolean removeShift(String storeID, String shiftID);
    boolean resolveShiftConflict(String storeID, Shift shift);
    boolean addShift(String storeID, Shift shift);
}

