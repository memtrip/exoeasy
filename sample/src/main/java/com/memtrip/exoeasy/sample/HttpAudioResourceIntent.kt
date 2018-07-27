package com.memtrip.exoeasy.sample

import android.content.Intent

import com.memtrip.exoeasy.AudioResourceIntent
import com.memtrip.exoeasy.NotificationInfo

class HttpAudioResourceIntent : AudioResourceIntent<HttpAudioResource>() {

    override fun into(data: HttpAudioResource, notificationInfo: NotificationInfo, intent: Intent): Intent {
        intent.putExtra(EXTRA_PROPERTY, data.extraProperty)
        return super.into(data, notificationInfo, intent)
    }

    override fun get(intent: Intent): HttpAudioResource {
        return HttpAudioResource(
            intent.getStringExtra(HTTP_AUDIO_STREAM_URL),
            intent.getStringExtra(EXTRA_PROPERTY),
            intent.getStringExtra(HTTP_AUDIO_STREAM_USER_AGENT),
            intent.getBooleanExtra(HTTP_AUDIO_STREAM_TRACK_PROGRESS, true)
        )
    }

    companion object {
        const val EXTRA_PROPERTY = "EXTRA_PROPERTY"
    }
}