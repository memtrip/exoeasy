package com.memtrip.exoeasy.player

import android.content.SharedPreferences

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class PlayerProgressTrackerTest : Spek({

    given("a PlayerProgressTracker with trackProgress turned Off") {

        val editor by memoized {
            mock<SharedPreferences.Editor>()
        }

        val sharedPreferences by memoized {
            mock<SharedPreferences> {
                on { edit() }.thenReturn(editor)
            }
        }

        val playerProgressTracker by memoized {
            PlayerProgressTracker(false, "ipfs://file", mock(), sharedPreferences)
        }

        on("tracking between 1000 and 9000 progress") {

            whenever(editor.putLong("PROGRESS_TRACKER_DURATION", 9000)).thenReturn(editor)
            whenever(editor.putString("PROGRESS_TRACKER_URL", "ipfs://file")).thenReturn(editor)

            playerProgressTracker.track(1000)
            playerProgressTracker.track(9000)

            it("should not commit progress to shared preferences") {
                verify(sharedPreferences.edit(), never()).putLong("PROGRESS_TRACKER_DURATION", 9000)
                verify(sharedPreferences.edit(), never()).putString("PROGRESS_TRACKER_URL", "ipfs://file")
            }
        }
    }

    given("a PlayerProgressTracker with trackProgress turned On") {

        val editor by memoized {
            mock<SharedPreferences.Editor>()
        }

        val sharedPreferences by memoized {
            mock<SharedPreferences> {
                on { edit() }.thenReturn(editor)
            }
        }

        val playerProgressTracker by memoized {
            PlayerProgressTracker(true, "ipfs://file", mock(), sharedPreferences)
        }

        on("tracking initial progress") {

            playerProgressTracker.track(1000)

            it("should not commit progress to shared preferences") {
                verify(sharedPreferences.edit(), never()).putLong("PROGRESS_TRACKER_DURATION", 1000)
                verify(sharedPreferences.edit(), never()).putString("PROGRESS_TRACKER_URL", "ipfs://file")
            }
        }

        on("tracking between 1000 and 3000 progress") {

            playerProgressTracker.track(1000)
            playerProgressTracker.track(3000)

            it("should not commit progress to shared preferences") {
                verify(sharedPreferences.edit(), never()).putLong("PROGRESS_TRACKER_DURATION", 1000)
                verify(sharedPreferences.edit(), never()).putString("PROGRESS_TRACKER_URL", "ipfs://file")
            }
        }

        on("tracking between 1000 and 9000 progress") {

            whenever(editor.putLong("PROGRESS_TRACKER_DURATION", 9000)).thenReturn(editor)
            whenever(editor.putString("PROGRESS_TRACKER_URL", "ipfs://file")).thenReturn(editor)

            playerProgressTracker.track(1000)
            playerProgressTracker.track(9000)

            it("should commit progress to shared preferences") {
                verify(sharedPreferences.edit()).putLong("PROGRESS_TRACKER_DURATION", 9000)
                verify(sharedPreferences.edit()).putString("PROGRESS_TRACKER_URL", "ipfs://file")
            }
        }

        on("tracking between 9000 and 1000 progress") {

            whenever(editor.putLong("PROGRESS_TRACKER_DURATION", 9000)).thenReturn(editor)
            whenever(editor.putString("PROGRESS_TRACKER_URL", "ipfs://file")).thenReturn(editor)

            playerProgressTracker.track(9000)
            playerProgressTracker.track(1000)

            it("should commit progress to shared preferences") {
                verify(sharedPreferences.edit()).putLong("PROGRESS_TRACKER_DURATION", 9000)
                verify(sharedPreferences.edit()).putString("PROGRESS_TRACKER_URL", "ipfs://file")
            }
        }

        on("retrieving current duration when no progress has been tracked") {

            whenever(editor.clear()).thenReturn(editor)

            val duration = playerProgressTracker.currentProgress("ipfs://file")

            it("should return a duration of 0 and clear shared preferences") {
                assertEquals(0, duration)
                verify(sharedPreferences.edit()).clear()
            }
        }

        on("retrieving current duration when progress has been tracked and the URLs match") {

            whenever(sharedPreferences.getLong("PROGRESS_TRACKER_DURATION", 0)).thenReturn(9000)
            whenever(sharedPreferences.getString("PROGRESS_TRACKER_URL", null)).thenReturn("ipfs://file")

            val duration = playerProgressTracker.currentProgress("ipfs://file")

            it("should return a duration of 0 and clear shared preferences") {
                assertEquals(9000, duration)
                verify(sharedPreferences.edit(), never()).clear()
            }
        }

        on("retrieving current duration when progress has been tracked and the URLs do not match") {

            whenever(editor.clear()).thenReturn(editor)
            whenever(sharedPreferences.getLong("PROGRESS_TRACKER_DURATION", 0)).thenReturn(9000)
            whenever(sharedPreferences.getString("PROGRESS_TRACKER_URL", null)).thenReturn("ipfs://file")

            val duration = playerProgressTracker.currentProgress("ipfs://different_file")

            it("should return a duration of 0 and clear shared preferences") {
                assertEquals(0, duration)
                verify(sharedPreferences.edit()).clear()
            }
        }
    }
})