package ru.mbg.palbociclib;

import android.app.Application;
import android.preference.PreferenceManager;

import java.util.Date;

import ru.mbg.palbociclib.helpers.AvatarHelper;
import ru.mbg.palbociclib.helpers.DatabaseHelper;
import ru.mbg.palbociclib.helpers.DateHelper;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseHelper.setupDatabase(true, this);
        AvatarHelper.initCache();

        long mockDate = PreferenceManager.getDefaultSharedPreferences(this).getLong(Constants.MOCK_DATE_KEY, -1);
        if (mockDate != -1) {
            DateHelper.instance.mockDate = new Date(mockDate);
        }
    }
}
