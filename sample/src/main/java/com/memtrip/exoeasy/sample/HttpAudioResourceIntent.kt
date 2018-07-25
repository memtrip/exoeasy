package com.memtrip.exoeasy.sample

import android.content.Intent

import com.memtrip.exoeasy.AudioResourceIntent
import com.memtrip.exoeasy.NotificationInfo

class HttpAudioResourceIntent : AudioResourceIntent<HttpAudioResource>() {

    override fun into(data: HttpAudioResource, notificationInfo: NotificationInfo, intent: Intent): Intent {
        intent.putExtra(HTTP_AUDIO_STREAM_URL, data.url)
        intent.putExtra(HTTP_AUDIO_STREAM_USER_AGENT, data.userAgent)
        intent.putExtra(HTTP_AUDIO_STREAM_TRACK_PROGRESS, data.trackProgress)
        return super.into(data, notificationInfo, intent)
    }

    override fun get(intent: Intent): HttpAudioResource {
        return HttpAudioResource(
            intent.getStringExtra(HTTP_AUDIO_STREAM_URL),
            intent.getStringExtra(HTTP_AUDIO_STREAM_USER_AGENT),
            intent.getBooleanExtra(HTTP_AUDIO_STREAM_TRACK_PROGRESS, true)
        )
    }

    companion object {
        const val HTTP_AUDIO_STREAM_URL = "HTTP_AUDIO_STREAM_URL"
        const val HTTP_AUDIO_STREAM_USER_AGENT = "HTTP_AUDIO_STREAM_USER_AGENT"
        const val HTTP_AUDIO_STREAM_TRACK_PROGRESS = "HTTP_AUDIO_STREAM_TRACK_PROGRESS"
    }
}