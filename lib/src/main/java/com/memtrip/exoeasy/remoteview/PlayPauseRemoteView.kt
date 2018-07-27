package com.memtrip.exoeasy.remoteview

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.memtrip.exoeasy.AudioResource

import com.memtrip.exoeasy.R
import com.memtrip.exoeasy.notification.PlayBackStateRemoteView
import com.memtrip.exoeasy.notification.Destination

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
class PlayPauseRemoteView<A : AudioResource>(
    context: Context,
    destination: Destination<A>
) : PlayBackStateRemoteView<A>(R.layout.notification_play_pause, destination, context) {

    override fun renderPlayState(remoteViews: RemoteViews): RemoteViews {
        remoteViews.setViewVisibility(R.id.notification_play_pause_play_button, View.GONE)
        remoteViews.setViewVisibility(R.id.notification_play_pause_pause_button, View.VISIBLE)
        remoteViews.setOnClickPendingIntent(R.id.notification_play_pause_pause_button, pausePendingIntent())
        return remoteViews
    }

    override fun renderPauseState(remoteViews: RemoteViews): RemoteViews {
        remoteViews.setViewVisibility(R.id.notification_play_pause_play_button, View.VISIBLE)
        remoteViews.setViewVisibility(R.id.notification_play_pause_pause_button, View.GONE)
        remoteViews.setOnClickPendingIntent(R.id.notification_play_pause_play_button, playPendingIntent())
        return remoteViews
    }

    override fun renderStopState(remoteViews: RemoteViews): RemoteViews {
        return renderPauseState(remoteViews)
    }
}