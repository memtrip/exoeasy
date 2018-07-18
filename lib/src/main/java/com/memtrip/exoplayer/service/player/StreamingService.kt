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

    private var player: Player? = null
    private var becomingNoisyInterrupt: InterruptBecomingNoisy? = null
    private var audioFocusInterrupt: InterruptAudioFocus? = null

    protected abstract fun audioResourceIntent(): AudioResourceIntent<T>

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val stream = audioResourceIntent().get(intent)

        start(stream)

        return Service.START_NOT_STICKY
    }

    private fun start(audioResource: T) {

        player = Player(
                audioResource.url(),
                BroadcastOnPlayerStateChanged(
                        audioResource.url(),
                        LocalBroadcastManager.getInstance(this),
                        Handler(Looper.getMainLooper())),
                this)

        becomingNoisyInterrupt = InterruptBecomingNoisy({ player!!.pause() }, application)
        becomingNoisyInterrupt!!.register()

        audioFocusInterrupt = InterruptAudioFocus({ player!!.pause() }, application)
        audioFocusInterrupt!!.register()

        player!!.prepare()
    }

    private fun release() {
        let { becomingNoisyInterrupt }?.unregister()
        let { audioFocusInterrupt }?.unregister()
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