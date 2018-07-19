package com.memtrip.exoplayer.service.player

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.memtrip.exoplayer.service.AudioResource
import com.memtrip.exoplayer.service.AudioResourceIntent
import com.memtrip.exoplayer.service.broadcast.BroadcastOnPlayerStateChanged

abstract class StreamingService<T : AudioResource> : Service() {

    private lateinit var playerFactory: PlayerFactory
    private lateinit var becomingNoisyInterrupt: InterruptBecomingNoisy
    private lateinit var audioFocusInterrupt: InterruptAudioFocus

    private var player: Player? = null

    protected abstract fun audioResourceIntent(): AudioResourceIntent<T>

    override fun onCreate() {
        super.onCreate()

        playerFactory = PlayerFactory(this)

        becomingNoisyInterrupt = InterruptBecomingNoisy({
            let { player }?.pause()
        }, application).register()

        audioFocusInterrupt = InterruptAudioFocus({
            let { player }?.pause()
        }, application).register()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        onIntentReceived(intent)
        return Service.START_NOT_STICKY
    }

    private fun onIntentReceived(intent: Intent) {

        val audioResource = audioResourceIntent().get(intent)

        player = playerFactory.get(
                audioResource,
                player,
                BroadcastOnPlayerStateChanged(
                audioResource.url(),
                LocalBroadcastManager.getInstance(this),
                Handler(Looper.getMainLooper())))

        AudioAction.perform(player!!, intent)
    }

    private fun release() {
        becomingNoisyInterrupt.unregister()
        audioFocusInterrupt.unregister()
        let { player }?.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        release()
        stopSelf()
    }

    override fun onBind(intent: Intent) = null
}