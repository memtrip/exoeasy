package com.memtrip.exoeasy

import android.content.Intent
import android.graphics.Bitmap
import androidx.annotation.CallSuper

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
abstract class AudioResourceIntent<A : AudioResource> {

    @CallSuper
    open fun into(data: A, notificationInfo: NotificationInfo, intent: Intent): Intent {
        intent.putExtra(HTTP_AUDIO_STREAM_NOTIFICATION_INFO_TITLE, notificationInfo.title)
        intent.putExtra(HTTP_AUDIO_STREAM_NOTIFICATION_INFO_BODY, notificationInfo.body)
        intent.putExtra(HTTP_AUDIO_STREAM_NOTIFICATION_INFO_ICON, notificationInfo.icon)
        return intent
    }

    abstract fun get(intent: Intent): A

    fun notificationInfo(intent: Intent): NotificationInfo {
        return NotificationInfo(
            intent.getStringExtra(HTTP_AUDIO_STREAM_NOTIFICATION_INFO_TITLE),
            intent.getStringExtra(HTTP_AUDIO_STREAM_NOTIFICATION_INFO_BODY),
            intent.getParcelableExtra(HTTP_AUDIO_STREAM_NOTIFICATION_INFO_ICON) as Bitmap?
        )
    }

    companion object {
        const val HTTP_AUDIO_STREAM_NOTIFICATION_INFO_TITLE = "HTTP_AUDIO_STREAM_NOTIFICATION_INFO_TITLE"
        const val HTTP_AUDIO_STREAM_NOTIFICATION_INFO_BODY = "HTTP_AUDIO_STREAM_NOTIFICATION_INFO_BODY"
        const val HTTP_AUDIO_STREAM_NOTIFICATION_INFO_ICON = "HTTP_AUDIO_STREAM_NOTIFICATION_INFO_ICON"
    }
}