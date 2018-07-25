package com.memtrip.exoeasy.player

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.MediaSource

import com.memtrip.exoeasy.AudioResource

internal class PlayerFactory<A : AudioResource>(private val context: Context) {

    fun get(
        audioResource: A,
        player: Player?,
        exoPlayer: ExoPlayer,
        mediaSource: MediaSource,
        onPlayerStateChanged: OnPlayerStateChanged
    ): Player {
        return if (player == null || player.audioResource.url != audioResource.url) {
            Player(audioResource, onPlayerStateChanged, context, exoPlayer, mediaSource)
        } else {
            player
        }
    }
}