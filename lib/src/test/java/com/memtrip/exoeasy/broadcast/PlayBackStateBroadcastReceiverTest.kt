package com.memtrip.exoeasy.broadcast

import android.content.Context
import android.content.Intent
import com.google.android.exoplayer2.ExoPlaybackException

import com.nhaarman.mockito_kotlin.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertEquals

import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import rx.subjects.PublishSubject

/**
 * Copyright 2013-present memtrip LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@RunWith(JUnitPlatform::class)
class PlayBackStateBroadcastReceiverTest : Spek({

    val context: Context = mock()

    given("a PlayBackStateBroadcastReceiver") {

        val publishSubject by memoized { PublishSubject.create<PlayBackState>() }
        val playBackStateBroadcastReceiver by memoized { PlayBackStateBroadcastReceiver(publishSubject) }

        on("a BroadcastType of BUFFERING") {

            val intent: Intent = mock {
                on {
                    getSerializableExtra("EXTRA_BROADCAST_TYPE")
                }.thenReturn(BroadcastType.BUFFERING)
            }

            val state = publishSubject.asObservable().test()

            playBackStateBroadcastReceiver.onReceive(context, intent)

            it("should return an PlayBackState of Buffering") {
                assertEquals(PlayBackState.Buffering, state.onNextEvents[0])
            }
        }

        on("a BroadcastType of Play") {

            val intent: Intent = mock {
                on {
                    getSerializableExtra("EXTRA_BROADCAST_TYPE")
                }.thenReturn(BroadcastType.PLAY)
            }

            val state = publishSubject.asObservable().test()

            playBackStateBroadcastReceiver.onReceive(context, intent)

            it("should return an PlayBackState of Play") {
                assertEquals(PlayBackState.Play, state.onNextEvents[0])
            }
        }

        on("a BroadcastType of Pause") {

            val intent: Intent = mock {
                on {
                    getSerializableExtra("EXTRA_BROADCAST_TYPE")
                }.thenReturn(BroadcastType.PAUSE)
            }

            val state = publishSubject.asObservable().test()

            playBackStateBroadcastReceiver.onReceive(context, intent)

            it("should return an PlayBackState of Pause") {
                assertEquals(PlayBackState.Pause, state.onNextEvents[0])
            }
        }

        on("a BroadcastType of Stop") {

            val intent: Intent = mock {
                on {
                    getSerializableExtra("EXTRA_BROADCAST_TYPE")
                }.thenReturn(BroadcastType.STOP)
            }

            val state = publishSubject.asObservable().test()

            playBackStateBroadcastReceiver.onReceive(context, intent)

            it("should return an PlayBackState of Stop") {
                assertEquals(PlayBackState.Stop, state.onNextEvents[0])
            }
        }

        on("a BroadcastType of Completed") {

            val intent: Intent = mock {
                on {
                    getSerializableExtra("EXTRA_BROADCAST_TYPE")
                }.thenReturn(BroadcastType.COMPLETED)
            }

            val state = publishSubject.asObservable().test()

            playBackStateBroadcastReceiver.onReceive(context, intent)

            it("should return an PlayBackState of Completed") {
                assertEquals(PlayBackState.Completed, state.onNextEvents[0])
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

            playBackStateBroadcastReceiver.onReceive(context, intent)

            it("should return an PlayBackState of Progress(10, 50, 100)") {
                assertEquals(PlayBackState.Progress(10, 50, 100), state.onNextEvents[0])
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

            playBackStateBroadcastReceiver.onReceive(context, intent)

            it("should return a PlayBackState of BufferingError(throwable)") {
                assertEquals(PlayBackState.BufferingError(throwable), state.onNextEvents[0])
            }
        }
    }
})