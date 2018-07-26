package com.memtrip.exoeasy.player

import android.content.Intent
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class AudioActionTest : Spek({

    given("a player being used in AudioAction") {

        val player by memoized { mock<Player>() }

        on("a Play AudioAction") {

            val intent = mock<Intent> {
                on {
                    getSerializableExtra("EXTRA_AUDIO_ACTION_TYPE")
                }.thenReturn(AudioAction.PLAY)
            }

            AudioAction.perform(player, intent)

            it("should trigger `play` on player") {
                verify(player).play()
            }
        }

        on("a Pause AudioAction") {

            val intent = mock<Intent> {
                on {
                    getSerializableExtra("EXTRA_AUDIO_ACTION_TYPE")
                }.thenReturn(AudioAction.PAUSE)
            }

            AudioAction.perform(player, intent)

            it("should trigger `pause` on player") {
                verify(player).pause()
            }
        }

        on("a Stop AudioAction") {

            val intent = mock<Intent> {
                on {
                    getSerializableExtra("EXTRA_AUDIO_ACTION_TYPE")
                }.thenReturn(AudioAction.STOP)
            }

            AudioAction.perform(player, intent)

            it("should trigger `stop` on player") {
                verify(player).stop()
            }
        }

        on("a Seek AudioAction") {

            val intent = mock<Intent> {
                on {
                    getSerializableExtra("EXTRA_AUDIO_ACTION_TYPE")
                }.thenReturn(AudioAction.SEEK)

                on {
                    getIntExtra("EXTRA_AUDIO_SEEK_PERCENTAGE", 0)
                }.thenReturn(80)
            }

            AudioAction.perform(player, intent)

            it("should trigger `seek` on player with a progress of 80") {
                verify(player).seek(80)
            }
        }

        on("a Tickle AudioAction") {

            val intent = mock<Intent> {
                on {
                    getSerializableExtra("EXTRA_AUDIO_ACTION_TYPE")
                }.thenReturn(AudioAction.TICKLE)
            }

            AudioAction.perform(player, intent)

            it("should trigger `tickle` on player") {
                verify(player).tickle()
            }
        }
    }
})