package com.memtrip.exoeasy.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.memtrip.exoeasy.AudioState
import com.memtrip.exoeasy.broadcast.AudioStateUpdates
import com.memtrip.exoeasy.player.AudioAction
import com.memtrip.exoeasy.secondsProgressFormat
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

        audio_playing_activity_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    seek(progress)
                }
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) { }

            override fun onStopTrackingTouch(seekbar: SeekBar?) { }
        })

        subject.subscribe {
            stateChanges(it)
        }
    }

    private fun seek(progress: Int) {
        startService(AudioAction.seek(progress, Intent(this, AudioStreamingService::class.java)))
    }

    private fun stateChanges(audioState: AudioState): Unit = when(audioState) {
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
            audio_playing_activity_play.visibility = View.VISIBLE
            audio_playing_activity_pause.visibility = View.GONE
        }
        is AudioState.Progress -> {
            audio_playing_activity_seekbar.progress = audioState.percentage
            audio_playing_activity_duration.text = audioState.duration.secondsProgressFormat()
            audio_playing_activity_progress.text = audioState.currentPosition.secondsProgressFormat()
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