package ru.mbg.palbociclib.models;


public enum BackgroundTherapyType {
    none(0),
    letrozole(1),
    fulvestrant(2);

    public final int rawValue;

    BackgroundTherapyType(int rawValue) {
        this.rawValue = rawValue;
    }

    public String description() {
        switch (this) {
            case fulvestrant:
                return "ФУЛВЕСТРАНТ 500Mg в/м\nНа 1й, 15й, 29 й день лечения, далее раз в месяц";
            case letrozole:
                return "ЛЕТРОЗОЛ 2,5 Mg\nкаждый день";
            case none:
            default:
                return "нет";
        }
    }

    public static BackgroundTherapyType fromRawValue(int type) {
        switch (type) {
            case 1:
                return letrozole;
            case 2:
                return fulvestrant;
            case 0:
            default:
                return none;
        }
    }
}
