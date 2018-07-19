package com.memtrip.exoeasy.broadcast

import android.content.Context
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.memtrip.exoeasy.AudioState
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