package com.memtrip.exoeasy.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.annotation.LayoutRes
import com.memtrip.exoeasy.AudioResource
import com.memtrip.exoeasy.broadcast.AudioState
import com.memtrip.exoeasy.player.AudioAction

abstract class AudioStateRemoteView<A : AudioResource>(
    @LayoutRes remoteViewLayout: Int,
    val destination: Destination<A>,
    val context: Context,
    private val remoteViews: RemoteViews = RemoteViews(context.packageName, remoteViewLayout)
) {

    protected fun playPendingIntent(): PendingIntent {
        return pendingIntent(context, AudioAction.PLAY.ordinal, AudioAction.play(destination.createServiceIntent()))
    }

    protected fun pausePendingIntent(): PendingIntent {
        return pendingIntent(context, AudioAction.PAUSE.ordinal, AudioAction.pause(destination.createServiceIntent()))
    }

    protected fun stopPendingIntent(): PendingIntent {
        return pendingIntent(context, AudioAction.STOP.ordinal, AudioAction.stop(destination.createServiceIntent()))
    }

    private fun pendingIntent(context: Context, requestCode: Int, intent: Intent): PendingIntent {
        return PendingIntent.getService(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    internal fun render(audioState: AudioState): RemoteViews = when (audioState) {
        AudioState.Buffering -> renderBufferingState(remoteViews)
        AudioState.Play -> renderPlayState(remoteViews)
        AudioState.Pause -> renderPauseState(remoteViews)
        AudioState.Stop -> renderStopState(remoteViews)
        AudioState.Completed -> renderCompletedState(remoteViews)
        is AudioState.Progress ->
            throw IllegalStateException("It would be too memory intensive to update the notification every second")
        is AudioState.BufferingError -> renderBufferingErrorState(
            audioState.throwable,
            remoteViews
        )
    }

    open fun renderBufferingState(remoteViews: RemoteViews): RemoteViews {
        return remoteViews
    }

    open fun renderPlayState(remoteViews: RemoteViews): RemoteViews {
        return remoteViews
    }

    open fun renderPauseState(remoteViews: RemoteViews): RemoteViews {
        return remoteViews
    }

    open fun renderStopState(remoteViews: RemoteViews): RemoteViews {
        return remoteViews
    }

    open fun renderCompletedState(remoteViews: RemoteViews): RemoteViews {
        return remoteViews
    }

    open fun renderBufferingErrorState(
        throwable: Throwable,
        remoteViews: RemoteViews
    ): RemoteViews {
        return remoteViews
    }
}