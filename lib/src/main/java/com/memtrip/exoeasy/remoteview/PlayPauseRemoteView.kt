package com.memtrip.exoeasy.remoteview

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.memtrip.exoeasy.AudioResource

import com.memtrip.exoeasy.R
import com.memtrip.exoeasy.notification.AudioStateRemoteView
import com.memtrip.exoeasy.notification.Destination

class PlayPauseRemoteView<A : AudioResource>(
    context: Context,
    destination: Destination<A>
) : AudioStateRemoteView<A>(R.layout.notification_play_pause, destination, context) {

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