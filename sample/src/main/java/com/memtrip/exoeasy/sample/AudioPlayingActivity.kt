package com.memtrip.exoeasy.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.memtrip.exoeasy.AudioStreamController
import com.memtrip.exoeasy.NotificationInfo
import com.memtrip.exoeasy.broadcast.PlayBackState
import com.memtrip.exoeasy.broadcast.PlayBackStateUpdates
import com.memtrip.exoeasy.secondsProgressFormat
import kotlinx.android.synthetic.main.audio_playing_activity.*

class AudioPlayingActivity : AppCompatActivity() {

    private lateinit var audioResource: HttpAudioResource

    private lateinit var audioStreamController: AudioStreamController<HttpAudioResource>

    private lateinit var playBackStateUpdates: PlayBackStateUpdates<HttpAudioResource>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_playing_activity)

        audioResource = HttpAudioResource(
            "https://s3.eu-west-2.amazonaws.com/rewindit-audio/Rewind+It+Really+Nice+Trips+%2311+by+Jason+Hogan+%2819-07-18%29.mp3",
            "extra property!")

        audioStreamController = AudioStreamController(
            audioResource,
            HttpAudioResourceIntent(),
            NotificationInfo("Jason Hogan", "This is it!", null),
            AudioStreamingService::class.java,
            this)

        playBackStateUpdates = PlayBackStateUpdates(audioResource)

        audio_playing_activity_play_button.setOnClickListener {
            audioStreamController.play()
        }

        audio_playing_activity_pause_button.setOnClickListener {
            audioStreamController.pause()
        }

        audio_playing_activity_stop.setOnClickListener {
            audioStreamController.stop()
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

        playBackStateUpdates.playBackStateChanges().subscribe {
            stateChanges(it)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val audioResource = intent?.let { HttpAudioResourceIntent().get(intent) }
        print(audioResource)
    }

    private fun seek(progress: Int) {
        audioStreamController.seek(progress)
    }

    private fun stateChanges(playBackState: PlayBackState): Unit = when (playBackState) {
        PlayBackState.Buffering -> {
            audio_playing_activity_progress.visibility = View.VISIBLE
            audio_playing_activity_play_button.visibility = View.GONE
            audio_playing_activity_pause_button.visibility = View.GONE
        }
        PlayBackState.Play -> {
            audio_playing_activity_progress.visibility = View.GONE
            audio_playing_activity_play_button.visibility = View.GONE
            audio_playing_activity_pause_button.visibility = View.VISIBLE
        }
        PlayBackState.Pause -> {
            audio_playing_activity_progress.visibility = View.GONE
            audio_playing_activity_play_button.visibility = View.VISIBLE
            audio_playing_activity_pause_button.visibility = View.GONE
        }
        PlayBackState.Stop -> {
            audio_playing_activity_progress.visibility = View.GONE
            audio_playing_activity_play_button.visibility = View.VISIBLE
            audio_playing_activity_pause_button.visibility = View.GONE
            audio_playing_activity_seekbar.progress = 0
        }
        PlayBackState.Completed -> {
            audio_playing_activity_progress.visibility = View.GONE
            audio_playing_activity_play_button.visibility = View.VISIBLE
            audio_playing_activity_pause_button.visibility = View.GONE
        }
        is PlayBackState.Progress -> {
            audio_playing_activity_progress.visibility = View.GONE
            audio_playing_activity_pause_button.visibility = View.VISIBLE
            audio_playing_activity_seekbar.progress = playBackState.percentage
            audio_playing_activity_duration_textview.text = playBackState.duration.secondsProgressFormat()
            audio_playing_activity_progress_textview.text = playBackState.currentPosition.secondsProgressFormat()
        }
        is PlayBackState.BufferingError -> {
            audio_playing_activity_progress.visibility = View.GONE
            audio_playing_activity_play_button.visibility = View.VISIBLE
            audio_playing_activity_pause_button.visibility = View.GONE
            audio_playing_activity_seekbar.progress = 0
        }
    }

    override fun onStart() {
        super.onStart()
        playBackStateUpdates.register(this)
        audioStreamController.tickle()
    }

    override fun onStop() {
        super.onStop()
        playBackStateUpdates.unregister(this)
    }
}