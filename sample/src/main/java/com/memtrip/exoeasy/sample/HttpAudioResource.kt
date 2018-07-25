package com.memtrip.exoeasy.sample

import com.memtrip.exoeasy.AudioResource

data class HttpAudioResource(
    override val url: String,
    override val userAgent: String = "${BuildConfig.VERSION_NAME}/${BuildConfig.VERSION_CODE}",
    override val trackProgress: Boolean = true
) : AudioResource