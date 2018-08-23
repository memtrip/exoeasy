package com.memtrip.sample_java;

import android.app.Activity;

import com.memtrip.exoeasy.AudioResourceIntent;
import com.memtrip.exoeasy.notification.Destination;
import com.memtrip.exoeasy.notification.NotificationConfig;
import com.memtrip.exoeasy.notification.PlayBackStateRemoteView;
import com.memtrip.exoeasy.player.StreamingService;
import com.memtrip.exoeasy.remoteview.PlayPauseStopRemoteView;
import com.memtrip.exoeasy.sample_java.R;

import org.jetbrains.annotations.NotNull;

public class AudioStreamingService extends StreamingService<HttpAudioResource> {

    @NotNull
    @Override
    protected AudioResourceIntent<HttpAudioResource> audioResourceIntent() {
        return new HttpAudioResourceIntent();
    }

    @NotNull
    @Override
    protected NotificationConfig notificationConfig() {
        return new NotificationConfig(
                true,
                getString(R.string.app_notification_channel_id),
                android.R.drawable.star_on);
    }

    @NotNull
    @Override
    protected Class<? extends Activity> activityDestination() {
        return AudioPlayingActivity.class;
    }

    @NotNull
    @Override
    protected PlayBackStateRemoteView<HttpAudioResource> playBackStateRemoteView(@NotNull Destination<HttpAudioResource> destination) {
        return new PlayPauseStopRemoteView<>(this, destination);
    }
}
