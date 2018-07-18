package sample.com.memtrip.exoplayer.service

import android.content.Intent

import com.memtrip.exoplayer.service.AudioResourceIntent

class RadioAudioStreamIntent : AudioResourceIntent<RadioAudioResource> {

    override fun into(data: RadioAudioResource, intent: Intent) {

    }

    override fun get(intent: Intent): RadioAudioResource {
        return RadioAudioResource()
    }
}