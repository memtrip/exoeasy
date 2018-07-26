package com.memtrip.exoeasy.notification

import androidx.annotation.DrawableRes

data class NotificationConfig(
    val showNotification: Boolean,
    val channelId: String = "",
    @DrawableRes val statusBarIcon: Int = -1
)