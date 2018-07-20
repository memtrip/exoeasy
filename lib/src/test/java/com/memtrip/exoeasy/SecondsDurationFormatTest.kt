package com.memtrip.exoeasy

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class SecondsDurationFormat : Spek({

    given("a secondsDurationFormat extension") {

        on("a duration of 40 seconds") {
            val duration: Long = 40

            it("formats as 00:40") {
                assertEquals("00:40", duration.secondsProgressFormat())
            }
        }

        on("a duration of 1000 seconds") {
            val duration: Long = 1000

            it("formats as 16:40") {
                assertEquals("16:40", duration.secondsProgressFormat())
            }
        }

        on("a duration of 10000 seconds") {
            val duration: Long = 10000

            it("formats as 2:46:40") {
                assertEquals("2:46:40", duration.secondsProgressFormat())
            }
        }
    }
})