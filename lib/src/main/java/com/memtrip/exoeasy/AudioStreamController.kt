package com.memtrip.exoeasy

import android.content.Context
import android.content.Intent
import com.memtrip.exoeasy.player.AudioAction
import com.memtrip.exoeasy.player.StreamingService
import kotlin.reflect.KClass

abstract class AudioStreamController<A : AudioResource>(
    private val notificationInfo: NotificationInfo,
    private val context: Context
) {

    fun play(audioResource: A) {
        val intent = Intent(context, streamingService().java)
        context.startService(AudioAction.play(intent().into(audioResource, notificationInfo, intent)))
    }

    fun pause(audioResource: A) {
        val intent = Intent(context, streamingService().java)
        context.startService(AudioAction.pause(intent().into(audioResource, notificationInfo, intent)))
    }

    fun stop(audioResource: A) {
        val intent = Intent(context, streamingService().java)
        context.startService(AudioAction.stop(intent().into(audioResource, notificationInfo, intent)))
    }

    fun seek(progress: Int, audioResource: A) {
        val intent = Intent(context, streamingService().java)
        context.startService(AudioAction.seek(progress, intent().into(audioResource, notificationInfo, intent)))
    }

    abstract fun intent(): AudioResourceIntent<A>

    abstract fun streamingService(): KClass<out StreamingService<A>>
}