package com.memtrip.exoplayer.service.broadcast

import android.content.Context
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.memtrip.exoplayer.service.AudioState
import rx.subjects.PublishSubject

class AudioStateUpdates(publishSubject: PublishSubject<AudioState>) {

    private val broadcastReceiver: AudioStateBroadcastReceiver = AudioStateBroadcastReceiver(publishSubject)

    fun register(context: Context) {
        LocalBroadcastManager
                .getInstance(context)
                .registerReceiver(broadcastReceiver, AudioStateBroadcastReceiver.intentFilter)
    }

    fun unregister(context: Context) {
        LocalBroadcastManager
                .getInstance(context)
                .unregisterReceiver(broadcastReceiver)
    }
}