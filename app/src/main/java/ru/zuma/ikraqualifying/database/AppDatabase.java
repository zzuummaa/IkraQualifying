package ru.zuma.ikraqualifying.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Конфигурации базы данных в
 * соответствии с требованиями
 * ORM DBFlow.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "AppDatabase";

    public static final int VERSION = 1;
}
