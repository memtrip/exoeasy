package com.memtrip.exoplayer.service.player

import android.content.Context

import com.memtrip.exoplayer.service.AudioResource

internal class PlayerFactory(private val context: Context) {

    fun get(
        audioResource: AudioResource,
        player: Player?,
        onPlayerStateChanged: OnPlayerStateChanged
    ): Player {
        return if (player == null || player.streamUrl != audioResource.url()) {
            Player(audioResource.url(), onPlayerStateChanged, context)
        } else {
            player
        }
    }
}