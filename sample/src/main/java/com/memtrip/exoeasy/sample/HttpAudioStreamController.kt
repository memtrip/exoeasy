package com.memtrip.exoeasy.sample

import android.content.Context
import com.memtrip.exoeasy.AudioStreamController
import kotlin.reflect.KClass

class HttpAudioStreamController(
    context: Context
) : AudioStreamController<HttpAudioResource>(context) {

    override fun intent(): HttpAudioResourceIntent = lazy { HttpAudioResourceIntent() }.value

    override fun streamingService(): KClass<AudioStreamingService> = AudioStreamingService::class
}