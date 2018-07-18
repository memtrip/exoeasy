package com.memtrip.exoplayer.service.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.memtrip.exoplayer.service.AudioState

import rx.subjects.PublishSubject

internal class AudioStateBroadcastReceiver(
    private val publishSubject: PublishSubject<AudioState>
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        publishSubject.onNext(playerState(intent))
    }

    private fun playerState(intent: Intent): AudioState = when (BroadcastOnPlayerStateChanged.broadcastType(intent)) {
        BroadcastType.BUFFERING -> AudioState.Buffering
        BroadcastType.PLAY -> AudioState.Play
        BroadcastType.PAUSE -> AudioState.Pause
        BroadcastType.STOP -> AudioState.Stop
        BroadcastType.COMPLETED -> AudioState.Completed
        BroadcastType.PROGRESS -> {
            AudioState.Progress(
                    BroadcastOnPlayerStateChanged.progressPercentage(intent),
                    BroadcastOnPlayerStateChanged.progressPosition(intent),
                    BroadcastOnPlayerStateChanged.progressDuration(intent))
        }
        BroadcastType.BUFFERING_ERROR -> {
            AudioState.BufferingError(BroadcastOnPlayerStateChanged.bufferError(intent))
        }
    }

    companion object {
        internal val intentFilter = IntentFilter(BroadcastOnPlayerStateChanged.ACTION_STREAM_NOTIFY)
    }
}