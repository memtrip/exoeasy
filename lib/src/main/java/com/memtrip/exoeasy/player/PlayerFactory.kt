package com.memtrip.exoeasy.player

import android.content.Context

import com.memtrip.exoeasy.AudioResource

internal class PlayerFactory(private val context: Context) {

    fun get(
        audioResource: AudioResource,
        player: Player?,
        onPlayerStateChanged: OnPlayerStateChanged
    ): Player {
        return if (player == null || player.config.streamUrl != audioResource.url()) {
            Player(PlayerConfig(
                audioResource.url(),
                audioResource.userAgent(),
                audioResource.trackProgress()
            ), onPlayerStateChanged, context)
        } else {
            player
        }
    }
}