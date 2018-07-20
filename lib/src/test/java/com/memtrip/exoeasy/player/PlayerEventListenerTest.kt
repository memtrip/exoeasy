package com.memtrip.exoeasy.player

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import junit.framework.Assert.assertTrue

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

import com.google.android.exoplayer2.Player as PlayerState

@RunWith(JUnitPlatform::class)
class PlayerEventListenerTest : Spek({

    given("a PlayerEventListener") {

        val onPlayerStateListener by memoized { mock<OnPlayerStateChanged>() }
        val playerProgressTick by memoized { mock<PlayerProgressTick>() }

        val playerEventListener by memoized { PlayerEventListener(onPlayerStateListener, playerProgressTick) }

        on("a PlayerState.STATE_BUFFERING") {

            playerEventListener.onPlayerStateChanged(false, PlayerState.STATE_BUFFERING)

            it("should stop ticker and notify buffering") {
                verify(playerProgressTick).stop()
                verify(onPlayerStateListener).onBuffering()
            }
        }

        on("a PlayerState.STATE_READY and playerWhenReady is true") {

            playerEventListener.onPlayerStateChanged(true, PlayerState.STATE_READY)

            it("should start ticker and notify `play`") {
                verify(playerProgressTick).stop()
                verify(playerProgressTick).start()
                verify(onPlayerStateListener).onPlay()
            }
        }

        on("a PlayerState.STATE_READY and playerWhenReady is false") {

            playerEventListener.onPlayerStateChanged(false, PlayerState.STATE_READY)

            it("should stop ticker and notify `paused`") {
                verify(playerProgressTick).stop()
                verify(onPlayerStateListener).onPause()
            }
        }

        on("a PlayerState.STATE_IDLE and playerWhenReady is false") {

            var onStopCalled = false

            playerEventListener.onStop = {
                onStopCalled = true
            }
            
            playerEventListener.onPlayerStateChanged(false, PlayerState.STATE_IDLE)

            it("should stop ticker, notify `stop` and trigger onStop callback") {
                verify(playerProgressTick).stop()
                verify(onPlayerStateListener).onStop()
                assertTrue(onStopCalled)
            }
        }
    }
})