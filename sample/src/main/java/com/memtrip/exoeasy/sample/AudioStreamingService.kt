package com.memtrip.exoeasy.sample

import com.memtrip.exoeasy.AudioResourceIntent
import com.memtrip.exoeasy.player.StreamingService

class AudioStreamingService : StreamingService<RadioAudioResource>() {

    override fun audioResourceIntent(): AudioResourceIntent<RadioAudioResource> = RadioAudioStreamIntent()
}