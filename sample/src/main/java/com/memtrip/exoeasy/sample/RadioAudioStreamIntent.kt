package com.memtrip.exoeasy.sample

import android.content.Intent

import com.memtrip.exoeasy.AudioResourceIntent

class RadioAudioStreamIntent : AudioResourceIntent<RadioAudioResource> {

    override fun into(data: RadioAudioResource, intent: Intent) {

    }

    override fun get(intent: Intent): RadioAudioResource {
        return RadioAudioResource()
    }
}