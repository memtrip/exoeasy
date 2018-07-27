package com.memtrip.exoeasy

import android.content.Context
import android.content.Intent
import com.memtrip.exoeasy.player.PlayBackAction
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
class AudioStreamController<A : AudioResource>(
    private val audioResource: A,
    private val audioResourceIntent: AudioResourceIntent<A>,
    private val notificationInfo: NotificationInfo,
    private val streamingService: KClass<out StreamingService<A>>,
    private val context: Context
) {

    fun play() {
        val intent = Intent(context, streamingService.java)
        context.startService(PlayBackAction.play(audioResourceIntent.into(audioResource, notificationInfo, intent)))
    }

    fun pause() {
        val intent = Intent(context, streamingService.java)
        context.startService(PlayBackAction.pause(audioResourceIntent.into(audioResource, notificationInfo, intent)))
    }

    fun stop() {
        val intent = Intent(context, streamingService.java)
        context.startService(PlayBackAction.stop(audioResourceIntent.into(audioResource, notificationInfo, intent)))
    }

    fun seek(progress: Int) {
        val intent = Intent(context, streamingService.java)
        context.startService(PlayBackAction.seek(progress, audioResourceIntent.into(audioResource, notificationInfo, intent)))
    }

    fun tickle() {
        val intent = Intent(context, streamingService.java)
        context.startService(PlayBackAction.tickle(audioResourceIntent.into(audioResource, notificationInfo, intent)))
    }
}