package ru.zuma.ikraqualifying;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Класс Android-приложения.
 * Реализация необходима
 * для инициализации ORM DBFlow
 * и первичного заполнения базы.
 */
public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        FlowManager.init(this);
    }

    public static Context getContext() {
        return context;
    }
}
