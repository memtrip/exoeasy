package sample.com.memtrip.exoplayer.service

import com.memtrip.exoplayer.service.AudioResource

class RadioAudioResource : AudioResource {

    override fun url(): String = "https://s3.eu-west-2.amazonaws.com/rewindit-audio/Rewind+It+Onda+Efimera+%2312+by+Skinnybone+Love+%2817-07-18%29+.mp3"
}