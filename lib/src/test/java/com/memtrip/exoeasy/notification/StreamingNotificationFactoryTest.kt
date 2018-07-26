package com.memtrip.exoeasy.notification

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import com.memtrip.exoeasy.AudioResource

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
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
class StreamingNotificationFactoryTest : Spek({

    given("a streamingNotificationFactory") {

        val config by memoized { mock<NotificationConfig>() }

        val audioStateRemoteView by memoized {
            mock<AudioStateRemoteView<AudioResource>> {
                on { destination }.thenReturn(mock())
            }
        }

        val context by memoized { mock<Context>() }

        val notificationManager by memoized { mock<NotificationManager>() }

        val streamingNotificationFactory by memoized {
            StreamingNotificationFactory(
                config,
                audioStateRemoteView,
                context,
                notificationManager
            )
        }

        on("a update notification with showNotification set to false") {

            val intent = mock<Intent>()

            whenever(config.showNotification).thenReturn(false)

            streamingNotificationFactory.update(intent)

            it("should not trigger notification manager update") {
                verify(notificationManager, never()).notify(any(), any())
            }
        }
    }
})