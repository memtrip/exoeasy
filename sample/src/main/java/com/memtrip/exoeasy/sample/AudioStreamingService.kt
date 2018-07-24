package com.memtrip.exoeasy.sample

import com.memtrip.exoeasy.AudioResourceIntent
import com.memtrip.exoeasy.player.StreamingService

class AudioStreamingService(
    private val httpAudioStreamIntent: HttpAudioResourceIntent = HttpAudioResourceIntent()
) : StreamingService<HttpAudioResource>() {

    override fun audioResourceIntent(): AudioResourceIntent<HttpAudioResource> = httpAudioStreamIntent
}