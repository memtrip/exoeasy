package com.memtrip.exoeasy.broadcast

import android.content.Intent

import android.os.Handler
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.memtrip.exoeasy.AudioResource

import com.memtrip.exoeasy.notification.StreamingNotificationFactory
import com.memtrip.exoeasy.player.OnPlayerStateChanged

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
internal class BroadcastOnPlayerStateChanged<A : AudioResource>(
    private val url: String,
    private val notificationFactory: StreamingNotificationFactory<A>,
    private val broadcastManager: LocalBroadcastManager,
    private val mainThreadHandler: Handler
) : OnPlayerStateChanged {

    override fun onBuffering() {
        mainThreadHandler.post {
            val intent = notifyIntent()
            intent.putExtra(EXTRA_BROADCAST_TYPE, BroadcastType.BUFFERING)
            broadcastManager.sendBroadcast(intent)
            notificationFactory.update(intent)
        }
    }

    override fun onPlay() {
        mainThreadHandler.post {
            val intent = notifyIntent()
            intent.putExtra(EXTRA_BROADCAST_TYPE, BroadcastType.PLAY)
            broadcastManager.sendBroadcast(intent)
            notificationFactory.update(intent)
        }
    }

    override fun onPause() {
        mainThreadHandler.post {
            val intent = notifyIntent()
            intent.putExtra(EXTRA_BROADCAST_TYPE, BroadcastType.PAUSE)
            broadcastManager.sendBroadcast(intent)
            notificationFactory.update(intent)
        }
    }

    override fun onStop() {
        mainThreadHandler.post {
            val intent = notifyIntent()
            intent.putExtra(EXTRA_BROADCAST_TYPE, BroadcastType.STOP)
            broadcastManager.sendBroadcast(intent)
            notificationFactory.update(intent)
        }
    }

    override fun onCompleted() {
        mainThreadHandler.post {
            val intent = notifyIntent()
            intent.putExtra(EXTRA_BROADCAST_TYPE, BroadcastType.COMPLETED)
            broadcastManager.sendBroadcast(intent)
            notificationFactory.update(intent)
        }
    }

    override fun onProgress(percentage: Int, currentPosition: Long, duration: Long) {
        mainThreadHandler.post {
            val intent = notifyIntent()
            intent.putExtra(EXTRA_BROADCAST_TYPE, BroadcastType.PROGRESS)
            intent.putExtra(EXTRA_BROADCAST_PROGRESS_PERCENTAGE, percentage)
            intent.putExtra(EXTRA_BROADCAST_PROGRESS_POSITION, currentPosition)
            intent.putExtra(EXTRA_BROADCAST_PROGRESS_DURATION, duration)
            broadcastManager.sendBroadcast(intent)
        }
    }

    override fun onBufferingError(throwable: Throwable) {
        mainThreadHandler.post {
            val intent = notifyIntent()
            intent.putExtra(EXTRA_BROADCAST_TYPE, BroadcastType.BUFFERING_ERROR)
            intent.putExtra(EXTRA_BROADCAST_BUFFERING_ERROR_THROWABLE, throwable)
            broadcastManager.sendBroadcast(intent)
            notificationFactory.update(intent)
        }
    }

    private fun notifyIntent(): Intent {
        val intent = Intent()
        intent.action = ACTION_STREAM_NOTIFY
        intent.type = url
        return intent
    }

    companion object {

        internal const val ACTION_STREAM_NOTIFY = "ACTION_STREAM_NOTIFY"
        private const val EXTRA_BROADCAST_TYPE = "EXTRA_BROADCAST_TYPE"
        private const val EXTRA_BROADCAST_PROGRESS_PERCENTAGE = "EXTRA_BROADCAST_PROGRESS_PERCENTAGE"
        private const val EXTRA_BROADCAST_PROGRESS_POSITION = "EXTRA_BROADCAST_PROGRESS_POSITION"
        private const val EXTRA_BROADCAST_PROGRESS_DURATION = "EXTRA_BROADCAST_PROGRESS_DURATION"
        private const val EXTRA_BROADCAST_BUFFERING_ERROR_THROWABLE = "EXTRA_BROADCAST_BUFFERING_ERROR_THROWABLE"

        internal fun broadcastType(intent: Intent): BroadcastType = intent.getSerializableExtra(EXTRA_BROADCAST_TYPE) as BroadcastType
        internal fun progressPercentage(intent: Intent): Int = intent.getIntExtra(EXTRA_BROADCAST_PROGRESS_PERCENTAGE, 0)
        internal fun progressPosition(intent: Intent): Long = intent.getLongExtra(EXTRA_BROADCAST_PROGRESS_POSITION, 0)
        internal fun progressDuration(intent: Intent): Long = intent.getLongExtra(EXTRA_BROADCAST_PROGRESS_DURATION, 0)
        internal fun bufferError(intent: Intent): Throwable = intent.getSerializableExtra(EXTRA_BROADCAST_BUFFERING_ERROR_THROWABLE) as Throwable
    }
}