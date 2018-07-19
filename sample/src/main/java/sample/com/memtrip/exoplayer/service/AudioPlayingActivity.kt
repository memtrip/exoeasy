package sample.com.memtrip.exoplayer.service

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.memtrip.exoplayer.service.AudioState
import com.memtrip.exoplayer.service.broadcast.AudioStateUpdates
import com.memtrip.exoplayer.service.player.AudioAction
import kotlinx.android.synthetic.main.audio_playing_activity.*
import rx.subjects.PublishSubject

class AudioPlayingActivity: AppCompatActivity() {

    private val subject: PublishSubject<AudioState> = PublishSubject.create()

    private val audioStateUpdates = AudioStateUpdates(subject)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_playing_activity)

        audio_playing_activity_play.setOnClickListener {
            startService(AudioAction.play(Intent(this, AudioStreamingService::class.java)))
        }

        audio_playing_activity_pause.setOnClickListener {
            startService(AudioAction.pause(Intent(this, AudioStreamingService::class.java)))
        }

        subject.subscribe {
            stateChanges(it)
        }
    }

    fun stateChanges(audioState: AudioState): Unit = when(audioState) {
        AudioState.Buffering -> {

        }
        AudioState.Play -> {
            audio_playing_activity_play.visibility = View.GONE
            audio_playing_activity_pause.visibility = View.VISIBLE
        }
        AudioState.Pause -> {
            audio_playing_activity_play.visibility = View.VISIBLE
            audio_playing_activity_pause.visibility = View.GONE
        }
        AudioState.Stop -> {

        }
        AudioState.Completed -> {

        }
        is AudioState.Progress -> {

        }
        is AudioState.BufferingError -> {

        }
    }

    override fun onStart() {
        super.onStart()
        audioStateUpdates.register(this)
    }

    override fun onStop() {
        super.onStop()
        audioStateUpdates.unregister(this)
    }
}