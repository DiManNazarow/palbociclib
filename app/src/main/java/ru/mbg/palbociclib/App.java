package ru.mbg.palbociclib;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.preference.PreferenceManager;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.Date;

import ru.mbg.palbociclib.helpers.AvatarHelper;
import ru.mbg.palbociclib.helpers.DatabaseHelper;
import ru.mbg.palbociclib.helpers.DateHelper;
import ru.mbg.palbociclib.new_version.db.AppDatabase;

public class App extends Application {

    private AppDatabase mAppDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseHelper.setupDatabase(true, this);
        AvatarHelper.initCache();
        JodaTimeAndroid.init(this);

        long mockDate = PreferenceManager.getDefaultSharedPreferences(this).getLong(Constants.MOCK_DATE_KEY, -1);
        if (mockDate != -1) {
            DateHelper.instance.mockDate = new Date(mockDate);
        }

        mAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "PalbociclibBatabase").allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    public AppDatabase getAppDatabase() {
        return mAppDatabase;
    }
}
