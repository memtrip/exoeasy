package com.memtrip.exoeasy.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.memtrip.exoeasy.broadcast.AudioState
import com.memtrip.exoeasy.NotificationInfo
import com.memtrip.exoeasy.broadcast.AudioStateUpdates

import com.memtrip.exoeasy.secondsProgressFormat
import kotlinx.android.synthetic.main.audio_playing_activity.*
import rx.subjects.PublishSubject

class AudioPlayingActivity : AppCompatActivity() {

    private val subject: PublishSubject<AudioState> = PublishSubject.create()

    private val audioStateUpdates = AudioStateUpdates(subject)

    private lateinit var audioStreamController: HttpAudioStreamController

    private lateinit var audioResource: HttpAudioResource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_playing_activity)

        audioStreamController = HttpAudioStreamController(
            NotificationInfo("Jason Hogan", "This is it!", null),
            this)

        audioResource = HttpAudioResource(
            "https://s3.eu-west-2.amazonaws.com/rewindit-audio/Rewind+It+Really+Nice+Trips+%2311+by+Jason+Hogan+%2819-07-18%29.mp3")

        audio_playing_activity_play_button.setOnClickListener {
            audioStreamController.play(audioResource)
        }

        audio_playing_activity_pause_button.setOnClickListener {
            audioStreamController.pause(audioResource)
        }

        audio_playing_activity_stop.setOnClickListener {
            audioStreamController.stop(audioResource)
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        print("ok")
    }

    private fun seek(progress: Int) {
        audioStreamController.seek(progress, audioResource)
    }

    private fun stateChanges(audioState: AudioState): Unit = when (audioState) {
        AudioState.Buffering -> {
            audio_playing_activity_progress.visibility = View.VISIBLE
        }
        AudioState.Play -> {
            audio_playing_activity_progress.visibility = View.GONE
            audio_playing_activity_play_button.visibility = View.GONE
            audio_playing_activity_pause_button.visibility = View.VISIBLE
        }
        AudioState.Pause -> {
            audio_playing_activity_progress.visibility = View.GONE
            audio_playing_activity_play_button.visibility = View.VISIBLE
            audio_playing_activity_pause_button.visibility = View.GONE
        }
        AudioState.Stop -> {
            audio_playing_activity_progress.visibility = View.GONE
            audio_playing_activity_play_button.visibility = View.VISIBLE
            audio_playing_activity_pause_button.visibility = View.GONE
            audio_playing_activity_seekbar.progress = 0
        }
        AudioState.Completed -> {
            audio_playing_activity_progress.visibility = View.GONE
            audio_playing_activity_play_button.visibility = View.VISIBLE
            audio_playing_activity_pause_button.visibility = View.GONE
        }
        is AudioState.Progress -> {
            audio_playing_activity_progress.visibility = View.GONE
            audio_playing_activity_seekbar.progress = audioState.percentage
            audio_playing_activity_duration_textview.text = audioState.duration.secondsProgressFormat()
            audio_playing_activity_progress_textview.text = audioState.currentPosition.secondsProgressFormat()
        }
        is AudioState.BufferingError -> {
            audio_playing_activity_progress.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        audioStateUpdates.register(this, audioResource.url)
    }

    override fun onStop() {
        super.onStop()
        audioStateUpdates.unregister(this)
    }
}