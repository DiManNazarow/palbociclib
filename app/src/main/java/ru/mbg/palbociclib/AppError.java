package ru.mbg.palbociclib;

public class AppError extends Exception {
    public static final int notSupportedPatient = 1;
    public static final int oakNotAssigned = 2;
    public static final int wrongAppointmentDate = 3;
    public static final int selectedLargerDose = 4;
    public static final int wrongAppointmentState = 5;

    final int type;

    AppError(int type) {
        this.type = type;
    }
}
