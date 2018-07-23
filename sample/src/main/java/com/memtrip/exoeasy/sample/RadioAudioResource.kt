package com.memtrip.exoeasy.sample

import com.memtrip.exoeasy.AudioResource

class RadioAudioResource : AudioResource {

    override fun userAgent(): String = BuildConfig.APPLICATION_ID + "/" + BuildConfig.VERSION_NAME

    override fun url(): String = "https://s3.eu-west-2.amazonaws.com/rewindit-audio/Rewind+It+Onda+Efimera+%2312+by+Skinnybone+Love+%2817-07-18%29+.mp3"

    override fun trackProgress(): Boolean = true
}