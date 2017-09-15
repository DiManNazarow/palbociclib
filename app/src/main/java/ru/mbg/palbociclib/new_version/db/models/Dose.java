package ru.mbg.palbociclib.new_version.db.models;

import ru.mbg.palbociclib.R;

public enum  Dose {

    DOSE_125(125) {
        @Override
        public Dose lover() {
            return DOSE_100;
        }

        @Override
        public int description() {
            return R.string.new_version_continue_125;
        }
    },
    DOSE_100(100) {
        @Override
        public Dose lover() {
            return DOSE_75;
        }

        @Override
        public int description() {
            return R.string.new_version_continue_100;
        }
    },
    DOSE_75(75) {
        @Override
        public Dose lover() {
            return DOSE_75;
        }

        @Override
        public int description() {
            return R.string.new_version_continue_75;
        }
    };

    private int dose;

    Dose(int dose) {
        this.dose = dose;
    }

    public abstract Dose lover();

    public abstract int description();

    public int getDose(){
        return dose;
    }

    public static Dose getByDose(int dose){
        switch (dose){
            case 125: return DOSE_125;
            case 100: return DOSE_100;
            case 75: return DOSE_75;
            default: return DOSE_125;
        }
    }

}
