package com.memtrip.exoeasy.sample

import android.app.Application

class DefaultApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppNotificationChannel(this).create()
    }
}