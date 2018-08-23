package com.memtrip.sample_java;

import android.app.Application;

public class DefaultApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new AppNotificationChannel(this).create();
    }
}
