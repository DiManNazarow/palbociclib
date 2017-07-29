package ru.mbg.palbociclib.helpers;

import android.content.Context;
import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;
import ru.mbg.palbociclib.Constants;

/**
 * DatabaseHelper - создание, инициализация и обновление базы данных приложения.
 */

public class DatabaseHelper {
    private static final int DATABASE_VERSION = 1;

    private static boolean clearRealm = false;

    public static void setupDatabase(boolean deleteOnMigration, Context context) {
        Realm.init(context);

        RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                .schemaVersion(DATABASE_VERSION)
                .migration(new Migration());
        if( deleteOnMigration ) {
            builder = builder.deleteRealmIfMigrationNeeded();
        }
        RealmConfiguration realmConfiguration = builder.build();
        Realm.setDefaultConfiguration(realmConfiguration);

        Realm realm = Realm.getDefaultInstance();

        if( clearRealm ) {
            // Пересоздать базу данных
            Log.w(Constants.TAG, "clear database");
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
        }

        realm.close();

        Log.w(Constants.TAG, "database at " + realmConfiguration.getPath());
    }

    private static class Migration implements RealmMigration {
        @SuppressWarnings("UnusedAssignment")
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            // Если надо в результате миграции сбросить содержимое базы данных, то надо clearRealm присвоить значение true
            RealmSchema schema = realm.getSchema();

            Log.w(Constants.TAG, "migrate database from " + oldVersion + " to " + newVersion);

            if( oldVersion == 1 ) {
                Log.w(Constants.TAG, "migrate from " + oldVersion);
                schema.get("TABLENAME").addField("FIELDNAME", String.class);
                oldVersion += 1;
            }
        }
    }
}
