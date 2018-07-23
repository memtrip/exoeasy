package com.memtrip.exoeasy.player

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.MediaSource

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class PlayerTest : Spek({

    given("a Player") {

        val config by memoized { mock<PlayerConfig>() }
        val onPlayerStateListener by memoized { mock<OnPlayerStateChanged>() }
        val context by memoized { mock<Context>() }
        val exoPlayer by memoized { mock<ExoPlayer>() }
        val progressTracker by memoized { mock<PlayerProgressTracker>() }
        val progressTick by memoized { mock<PlayerProgressTick>() }
        val playerEventListener by memoized { mock<PlayerEventListener>() }
        val mediaSource by memoized { mock<MediaSource>() }

        val player by memoized {

            Player(
                config,
                onPlayerStateListener,
                context,
                exoPlayer,
                progressTracker,
                progressTick,
                playerEventListener,
                mediaSource)
        }

        on("`play` on player before preparation has completed, without a tracked progress") {

            player.play()

            it("prepare the player with the media source and set playWhenReady to true") {
                verify(exoPlayer).prepare(mediaSource)
                verify(exoPlayer, never()).seekTo(0)
                verify(exoPlayer).playWhenReady = true
            }
        }

        on("`play` on player before preparation has completed, with a tracked progress") {

            whenever(config.streamUrl).thenReturn("ipfs://file")
            whenever(progressTracker.currentProgress("ipfs://file")).thenReturn(1000)

            player.play()

            it("prepare the player with the media source and set playWhenReady to true") {
                verify(exoPlayer).prepare(mediaSource)
                verify(exoPlayer).seekTo(1000)
                verify(exoPlayer).playWhenReady = true
            }
        }

        on("`play` on player after preparation has completed") {

            player.play()
            player.play()

            it("should only ever prepare the player once") {
                verify(exoPlayer, times(1)).prepare(mediaSource)
                verify(exoPlayer, times(2)).playWhenReady = true
            }
        }

        on("`pause` on player") {

            player.pause()

            it("set playWhenReady to false") {
                verify(exoPlayer).playWhenReady = false
            }
        }

        on("`seek` on player") {

            whenever(exoPlayer.duration).thenReturn(1000)

            player.seek(20)

            it("calculate and seek to the desired position") {
                verify(exoPlayer).seekTo(200L)
            }
        }

        on("`release` on player") {

            player.release()

            it("stops and release the exopplayer") {
                verify(exoPlayer).stop()
                verify(exoPlayer).release()
            }
        }
    }
})