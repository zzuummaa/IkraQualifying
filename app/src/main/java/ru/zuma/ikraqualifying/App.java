package ru.zuma.ikraqualifying;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Класс Android-приложения.
 * Необходим для инициализации
 * ORM DBFlow.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
