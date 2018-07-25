package com.memtrip.exoeasy.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

import rx.subjects.PublishSubject

internal class AudioStateBroadcastReceiver(
    private val publishSubject: PublishSubject<AudioState>
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        publishSubject.onNext(AudioState.playerState(intent))
    }

    companion object {
        internal fun intentFilter(url: String): IntentFilter {
            return IntentFilter(BroadcastOnPlayerStateChanged.ACTION_STREAM_NOTIFY, url)
        }
    }
}