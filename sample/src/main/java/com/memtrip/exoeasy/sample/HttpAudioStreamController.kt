package com.memtrip.exoeasy.sample

import android.content.Context
import com.memtrip.exoeasy.AudioStreamController
import com.memtrip.exoeasy.NotificationInfo
import kotlin.reflect.KClass

class HttpAudioStreamController(
    notificationInfo: NotificationInfo,
    context: Context
) : AudioStreamController<HttpAudioResource>(notificationInfo, context) {

    override fun intent(): HttpAudioResourceIntent = lazy { HttpAudioResourceIntent() }.value

    override fun streamingService(): KClass<AudioStreamingService> = AudioStreamingService::class
}