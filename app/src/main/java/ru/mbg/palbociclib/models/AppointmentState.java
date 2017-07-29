package ru.mbg.palbociclib.models;

public enum AppointmentState {
    oakNeeded(0),
    done(1),
    assignNextAppointment(2),
    assignContinueTreatment(3),
    maybeLowerDose(4),
    doseWasReduced(5);

    public final int rawValue;

    AppointmentState(int rawValue) {
        this.rawValue = rawValue;
    }

    public static AppointmentState fromRawValue(int value) {
        switch (value) {
            default:
                return oakNeeded;
            case 1:
                return done;
            case 2:
                return assignNextAppointment;
            case 3:
                return assignContinueTreatment;
            case 4:
                return maybeLowerDose;
            case 5:
                return doseWasReduced;
        }
    }

    // Состояния, по которым дальнейших действий нет, прием окончен
    public boolean isTerminal() {
        return this == done || this == doseWasReduced;
    }
}
