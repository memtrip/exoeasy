package com.memtrip.exoeasy.broadcast

import android.content.Context
import android.content.Intent
import com.google.android.exoplayer2.ExoPlaybackException
import com.memtrip.exoeasy.AudioState

import com.nhaarman.mockito_kotlin.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertEquals

import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import rx.subjects.PublishSubject

@RunWith(JUnitPlatform::class)
class AudioStateBroadcastReceiverTest : Spek({

    val context: Context = mock()

    given("a AudioStateBroadcastReceiver") {

        val publishSubject by memoized { PublishSubject.create<AudioState>() }
        val audioStateBroadcastReceiver by memoized { AudioStateBroadcastReceiver(publishSubject) }

        on("a BroadcastType of BUFFERING") {

            val intent: Intent = mock {
                on {
                    getSerializableExtra("EXTRA_BROADCAST_TYPE")
                }.thenReturn(BroadcastType.BUFFERING)
            }

            val state = publishSubject.asObservable().test()

            audioStateBroadcastReceiver.onReceive(context, intent)

            it("should return an AudioState of Buffering") {
                assertEquals(AudioState.Buffering, state.onNextEvents[0])
            }
        }

        on("a BroadcastType of Play") {

            val intent: Intent = mock {
                on {
                    getSerializableExtra("EXTRA_BROADCAST_TYPE")
                }.thenReturn(BroadcastType.PLAY)
            }

            val state = publishSubject.asObservable().test()

            audioStateBroadcastReceiver.onReceive(context, intent)

            it("should return an AudioState of Play") {
                assertEquals(AudioState.Play, state.onNextEvents[0])
            }
        }

        on("a BroadcastType of Pause") {

            val intent: Intent = mock {
                on {
                    getSerializableExtra("EXTRA_BROADCAST_TYPE")
                }.thenReturn(BroadcastType.PAUSE)
            }

            val state = publishSubject.asObservable().test()

            audioStateBroadcastReceiver.onReceive(context, intent)

            it("should return an AudioState of Pause") {
                assertEquals(AudioState.Pause, state.onNextEvents[0])
            }
        }

        on("a BroadcastType of Stop") {

            val intent: Intent = mock {
                on {
                    getSerializableExtra("EXTRA_BROADCAST_TYPE")
                }.thenReturn(BroadcastType.STOP)
            }

            val state = publishSubject.asObservable().test()

            audioStateBroadcastReceiver.onReceive(context, intent)

            it("should return an AudioState of Stop") {
                assertEquals(AudioState.Stop, state.onNextEvents[0])
            }
        }

        on("a BroadcastType of Completed") {

            val intent: Intent = mock {
                on {
                    getSerializableExtra("EXTRA_BROADCAST_TYPE")
                }.thenReturn(BroadcastType.COMPLETED)
            }

            val state = publishSubject.asObservable().test()

            audioStateBroadcastReceiver.onReceive(context, intent)

            it("should return an AudioState of Completed") {
                assertEquals(AudioState.Completed, state.onNextEvents[0])
            }
        }

        on("a BroadcastType of Progress with a percentage of 10, position of 50, duration 100") {

            val intent: Intent = mock {
                on {
                    getSerializableExtra("EXTRA_BROADCAST_TYPE")
                }.thenReturn(BroadcastType.PROGRESS)

                on {
                    getIntExtra("EXTRA_BROADCAST_PROGRESS_PERCENTAGE", 0)
                }.thenReturn(10)

                on {
                    getLongExtra("EXTRA_BROADCAST_PROGRESS_POSITION", 0)
                }.thenReturn(50)

                on {
                    getLongExtra("EXTRA_BROADCAST_PROGRESS_DURATION", 0)
                }.thenReturn(100)
            }

            val state = publishSubject.asObservable().test()

            audioStateBroadcastReceiver.onReceive(context, intent)

            it("should return an AudioState of Progress(10, 50, 100)") {
                assertEquals(AudioState.Progress(10, 50, 100), state.onNextEvents[0])
            }
        }

        on("a BroadcastType of BufferingError with a throwable") {

            val throwable: ExoPlaybackException = mock()

            val intent: Intent = mock {
                on {
                    getSerializableExtra("EXTRA_BROADCAST_TYPE")
                }.thenReturn(BroadcastType.BUFFERING_ERROR)

                on {
                    getSerializableExtra("EXTRA_BROADCAST_BUFFERING_ERROR_THROWABLE")
                }.thenReturn(throwable)
            }

            val state = publishSubject.asObservable().test()

            audioStateBroadcastReceiver.onReceive(context, intent)

            it("should return a AudioState of BufferingError(throwable)") {
                assertEquals(AudioState.BufferingError(throwable), state.onNextEvents[0])
            }
        }
    }
})