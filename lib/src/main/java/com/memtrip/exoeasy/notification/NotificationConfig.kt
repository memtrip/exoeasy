package com.memtrip.exoeasy.notification

import androidx.annotation.DrawableRes

data class NotificationConfig(
    val channelId: String,
    @DrawableRes val statusBarIcon: Int
)