package com.memtrip.exoeasy.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.content.IntentFilter

internal class InterruptBecomingNoisy constructor(
    private val interruptAudio: InterruptAudio,
    private val context: Context,
    private val intentFilter: IntentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
) : BroadcastReceiver() {

    fun register(): InterruptBecomingNoisy {
        context.registerReceiver(this, intentFilter)
        return this
    }

    fun unregister() {
        context.unregisterReceiver(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {
            interruptAudio()
        }
    }
}