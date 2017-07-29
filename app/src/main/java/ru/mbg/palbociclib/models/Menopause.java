package ru.mbg.palbociclib.models;


public enum Menopause {
    none(0),
    postmenopause(1),
    menopause(2),
    perimenopause(3);

    public final int rawValue;

    Menopause(int rawValue) {
        this.rawValue = rawValue;
    }

    public String description() {
        switch (this) {
            case postmenopause:
                return "Постменопауза";
            case menopause:
                return "Менопауза";
            case perimenopause:
                return "Перименопауза";
            case none:
            default:
                return "Не задано";
        }
    }

    public static Menopause fromRawValue(int value) {
        switch (value) {
            case 1:
                return postmenopause;
            case 2:
                return menopause;
            case 3:
                return perimenopause;
            case 0:
            default:
                return none;
        }
    }

    public static Menopause fromDescription(String value) {
        switch (value) {
            case "Постменопауза":
                return postmenopause;
            case "Менопауза":
                return menopause;
            case "Перименопауза":
                return perimenopause;
            default:
                return none;
        }
    }
}
