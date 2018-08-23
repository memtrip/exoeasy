package com.memtrip.sample_java;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.memtrip.exoeasy.AudioStreamController;
import com.memtrip.exoeasy.NotificationInfo;
import com.memtrip.exoeasy.SecondsDurationFormatKt;
import com.memtrip.exoeasy.broadcast.PlayBackState;
import com.memtrip.exoeasy.broadcast.PlayBackStateUpdates;
import com.memtrip.exoeasy.sample_java.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class AudioPlayingActivity extends AppCompatActivity {

    private AudioStreamController<HttpAudioResource> audioStreamController;

    private PlayBackStateUpdates<HttpAudioResource> updates;

    @BindView(R.id.audio_playing_activity_play_button)
    Button playButton;

    @BindView(R.id.audio_playing_activity_pause_button)
    Button pauseButton;

    @BindView(R.id.audio_playing_activity_progress)
    ProgressBar progressBar;

    @BindView(R.id.audio_playing_activity_progress_textview)
    TextView progressTextView;

    @BindView(R.id.audio_playing_activity_duration_textview)
    TextView durationTextView;

    @BindView(R.id.audio_playing_activity_seekbar)
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_playing_activity);
        ButterKnife.bind(this);

        HttpAudioResource audioResource = new HttpAudioResource(
                "https://s3.eu-west-2.amazonaws.com/rewindit-audio/Rewind+It+Really+Nice+Trips+%2311+by+Jason+Hogan+%2819-07-18%29.mp3",
                "extra property!");

        audioStreamController = new AudioStreamController<>(
                audioResource,
                new HttpAudioResourceIntent(),
                new NotificationInfo("Jason Hogan", "This is it!", null),
                AudioStreamingService.class,
                this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioStreamController.seek(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {  }
        });

        updates = new PlayBackStateUpdates<>(audioResource);

        updates.playBackStateChanges().subscribe(new Action1<PlayBackState>() {
            @Override
            public void call(PlayBackState playBackState) {
                stateChanges(playBackState);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (updates != null) {
            updates.register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (updates != null) {
            updates.unregister(this);
        }
    }

    /**
     * This is elegant as a Kotlin sealed class / exhaustive when!
     */
    private void stateChanges(PlayBackState playBackState) {
        if (playBackState instanceof PlayBackState.Buffering) {
            progressBar.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.GONE);
        } else if (playBackState instanceof PlayBackState.Play) {
            progressBar.setVisibility(View.GONE);
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
        } else if (playBackState instanceof PlayBackState.Pause) {
            progressBar.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        } else if (playBackState instanceof PlayBackState.Stop) {
            progressBar.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
            seekBar.setProgress(0);
        } else if (playBackState instanceof PlayBackState.Completed) {
            progressBar.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        } else if (playBackState instanceof PlayBackState.Progress) {
            final PlayBackState.Progress progress = (PlayBackState.Progress) playBackState;
            progressBar.setVisibility(View.GONE);
            seekBar.setProgress(progress.getPercentage());
            progressTextView.setText(SecondsDurationFormatKt.secondsProgressFormat(progress.getCurrentPosition()));
            durationTextView.setText(SecondsDurationFormatKt.secondsProgressFormat(progress.getDuration()));
            pauseButton.setVisibility(View.VISIBLE);
        } else if (playBackState instanceof PlayBackState.BufferingError) {
            progressBar.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
            seekBar.setProgress(0);
        }
    }

    @OnClick(R.id.audio_playing_activity_play_button)
    void playClick() {
        audioStreamController.play();
    }

    @OnClick(R.id.audio_playing_activity_pause_button)
    void pauseClick() {
        audioStreamController.pause();
    }

    @OnClick(R.id.audio_playing_activity_stop_button)
    void stopClick() {
        audioStreamController.stop();
    }
}
