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

/**
 * Copyright 2013-present memtrip LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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