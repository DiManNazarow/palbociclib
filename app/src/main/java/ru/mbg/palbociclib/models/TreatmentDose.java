package ru.mbg.palbociclib.models;

import ru.mbg.palbociclib.R;


public enum TreatmentDose {
    none(0),
    dose75(1),
    dose100(2),
    dose125(3);

    public final int rawValue;

    TreatmentDose(int rawValue) {
        this.rawValue = rawValue;
    }

    public int getImageResource() {
        switch (this) {
            case dose75:
                return R.drawable.dose75;
            case dose100:
                return R.drawable.dose100;
            case dose125:
                return R.drawable.dose125;
            case none:
            default:
                return 0;
        }
    }

    public String description() {
        switch (this) {
            case dose75:
                return "75 мг";
            case dose100:
                return "100 мг";
            case dose125:
                return "125 мг";
            case none:
            default:
                return "";
        }
    }

    public static TreatmentDose fromRawValue(int value) {
        switch (value) {
            case 1:
                return dose75;
            case 2:
                return dose100;
            case 3:
                return dose125;
            case 0:
            default:
                return none;
        }
    }
}
