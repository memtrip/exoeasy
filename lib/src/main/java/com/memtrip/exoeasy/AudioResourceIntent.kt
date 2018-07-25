package com.memtrip.exoeasy

import android.content.Intent
import android.graphics.Bitmap
import androidx.annotation.CallSuper

abstract class AudioResourceIntent<A : AudioResource> {

    @CallSuper
    open fun into(data: A, notificationInfo: NotificationInfo, intent: Intent): Intent {
        intent.putExtra(HTTP_AUDIO_STREAM_NOTIFICATION_INFO_TITLE, notificationInfo.title)
        intent.putExtra(HTTP_AUDIO_STREAM_NOTIFICATION_INFO_BODY, notificationInfo.body)
        intent.putExtra(HTTP_AUDIO_STREAM_NOTIFICATION_INFO_ICON, notificationInfo.icon)
        return intent
    }

    abstract fun get(intent: Intent): A

    fun notificationInfo(intent: Intent): NotificationInfo {
        return NotificationInfo(
            intent.getStringExtra(HTTP_AUDIO_STREAM_NOTIFICATION_INFO_TITLE),
            intent.getStringExtra(HTTP_AUDIO_STREAM_NOTIFICATION_INFO_BODY),
            intent.getParcelableExtra(HTTP_AUDIO_STREAM_NOTIFICATION_INFO_ICON) as Bitmap?
        )
    }

    companion object {
        const val HTTP_AUDIO_STREAM_NOTIFICATION_INFO_TITLE = "HTTP_AUDIO_STREAM_NOTIFICATION_INFO_TITLE"
        const val HTTP_AUDIO_STREAM_NOTIFICATION_INFO_BODY = "HTTP_AUDIO_STREAM_NOTIFICATION_INFO_BODY"
        const val HTTP_AUDIO_STREAM_NOTIFICATION_INFO_ICON = "HTTP_AUDIO_STREAM_NOTIFICATION_INFO_ICON"
    }
}