package com.memtrip.exoeasy

import android.content.Context
import android.content.Intent
import com.memtrip.exoeasy.player.AudioAction
import com.memtrip.exoeasy.player.StreamingService
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

    fun tickle(audioResource: A) {
        val intent = Intent(context, streamingService().java)
        context.startService(AudioAction.tickle(intent().into(audioResource, notificationInfo, intent)))
    }

    abstract fun intent(): AudioResourceIntent<A>

    abstract fun streamingService(): KClass<out StreamingService<A>>
}