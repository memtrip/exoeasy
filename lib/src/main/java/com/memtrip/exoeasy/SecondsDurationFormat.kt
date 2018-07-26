package com.memtrip.exoeasy

import java.util.Locale

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
fun Long.secondsProgressFormat(): String {
    val hours = this / 3600
    val minutes = this % 3600 / 60
    val seconds = this % 60

    return if (hours > 0) {
        String.format(Locale.UK, "%2d:%02d:%02d", hours, minutes, seconds).trim()
    } else {
        String.format(Locale.UK, "%02d:%02d", minutes, seconds).trim()
    }
}