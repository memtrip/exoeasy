package com.memtrip.exoeasy.sample

import com.memtrip.exoeasy.AudioResource

class HttpAudioResource(
    private val url: String,
    private val userAgent: String = "${BuildConfig.VERSION_NAME}/${BuildConfig.VERSION_CODE}",
    private val trackProgress: Boolean = true
) : AudioResource {

    override fun url(): String = url

    override fun userAgent(): String = userAgent

    override fun trackProgress(): Boolean = trackProgress
}