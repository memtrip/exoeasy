package com.memtrip.exoeasy.sample

import com.memtrip.exoeasy.player.StreamingService

class AudioStreamingService : StreamingService<HttpAudioResource>() {

    override fun audioResourceIntent(): HttpAudioResourceIntent = HttpAudioResourceIntent()
}