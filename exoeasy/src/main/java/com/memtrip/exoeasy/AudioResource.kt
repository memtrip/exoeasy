package com.memtrip.exoeasy

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
interface AudioResource {
    /**
     * The URL of the audio resource
     */
    val url: String

    /**
     * Device user agent e.g.
     * ${BuildConfig.VERSION_NAME}/${BuildConfig.VERSION_CODE}
     */
    val userAgent: String

    /**
     * Track audio progress periodically with:
     * @see com.memtrip.exoeasy.broadcast.PlayBackState.Progress
     */
    val trackProgress: Boolean
}