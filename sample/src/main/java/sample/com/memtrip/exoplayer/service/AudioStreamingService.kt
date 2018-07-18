package sample.com.memtrip.exoplayer.service

import com.memtrip.exoplayer.service.AudioResourceIntent
import com.memtrip.exoplayer.service.player.StreamingService

class AudioStreamingService : StreamingService<RadioAudioResource>() {

    override fun audioResourceIntent(): AudioResourceIntent<RadioAudioResource> = RadioAudioStreamIntent()
}