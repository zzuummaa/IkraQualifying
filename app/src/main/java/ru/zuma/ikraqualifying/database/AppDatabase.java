package ru.zuma.ikraqualifying.database;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import ru.zuma.ikraqualifying.database.tables.UserDbModel;

/**
 * Конфигурации базы данных в
 * соответствии с требованиями
 * ORM DBFlow.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "AppDatabase";

    public static final int VERSION = 1;

    @Migration(version = 0, database = AppDatabase.class)
    public static class InitMigration extends BaseMigration {

        @Override
        public void migrate(@NonNull DatabaseWrapper database) {
            UserDbModel initUser = new UserDbModel();
            initUser.setName("Test");
            initUser.setSecondName("User");
            initUser.setThirdName("OfUser");
            initUser.setGroup("H11");
            initUser.setAbout("Ok!");
            initUser.insert(database);
        }
    }
}
