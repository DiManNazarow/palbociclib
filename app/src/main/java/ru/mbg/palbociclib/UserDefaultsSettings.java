package ru.mbg.palbociclib;

import android.content.Context;
import android.preference.PreferenceManager;

public class UserDefaultsSettings implements Settings {
    private static final String FIO = "settings.fio";
    private static final String OAK = "settings.oak_left";
    private static final String PUSH = "settings.push";

    private Context context;

    public UserDefaultsSettings(Context context) {
        this.context = context;
    }

    @Override
    public String getFio() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(FIO, "");
    }

    @Override
    public void setFio(String fio) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(FIO, fio).apply();
    }

    @Override
    public int getOakReadyDays() {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(OAK, 1);
    }

    @Override
    public void setOakReadyDays(int days) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(OAK, days).apply();
    }

    @Override
    public boolean isPushEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PUSH, false);
    }

    @Override
    public void setPushEnabled(boolean enabled) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PUSH, enabled).apply();
    }
}
