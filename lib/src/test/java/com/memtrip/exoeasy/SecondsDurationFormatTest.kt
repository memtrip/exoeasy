package com.memtrip.exoeasy

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

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