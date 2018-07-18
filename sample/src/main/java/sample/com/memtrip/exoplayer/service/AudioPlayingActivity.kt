package sample.com.memtrip.exoplayer.service

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.memtrip.exoplayer.service.AudioState
import com.memtrip.exoplayer.service.broadcast.AudioStateUpdates
import kotlinx.android.synthetic.main.audio_playing_activity.*
import rx.subjects.PublishSubject

class AudioPlayingActivity: AppCompatActivity() {

    private val subject: PublishSubject<AudioState> = PublishSubject.create()

    private val audioStateUpdates = AudioStateUpdates(subject)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_playing_activity)

        audio_playing_activity_toggle.setOnClickListener {
            startService(Intent(this, AudioStreamingService::class.java))
        }

        subject.subscribe {
            print(it)
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