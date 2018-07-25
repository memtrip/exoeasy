package com.memtrip.exoeasy.notification

import android.app.Activity
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import com.memtrip.exoeasy.AudioResource
import com.memtrip.exoeasy.AudioResourceIntent
import com.memtrip.exoeasy.NotificationInfo
import kotlin.reflect.KClass

class Destination<A : AudioResource>(
    private val audioResource: A,
    private val audioResourceIntent: AudioResourceIntent<A>,
    private val notificationInfo: NotificationInfo,
    private val destinationActivity: KClass<out Activity>,
    private val destinationService: KClass<out Service>,
    private val context: Context
) {

    internal fun createActivityIntent(): PendingIntent {
        val intent = Intent(context, destinationActivity.java)
        intent.action = ACTION_AUDIO_STREAM_NOTIFICATION
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(
            context,
            0,
            audioResourceIntent.into(audioResource, notificationInfo, intent),
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    internal fun createServiceIntent(): Intent {
        val intent = Intent(context, destinationService.java)
        return audioResourceIntent.into(
            audioResource,
            notificationInfo,
            intent)
    }

    companion object {
        const val ACTION_AUDIO_STREAM_NOTIFICATION = "ACTION_AUDIO_STREAM_NOTIFICATION"
    }
}