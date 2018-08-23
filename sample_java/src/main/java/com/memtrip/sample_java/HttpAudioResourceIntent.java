package com.memtrip.sample_java;

import android.content.Intent;

import com.memtrip.exoeasy.AudioResourceIntent;
import com.memtrip.exoeasy.NotificationInfo;

import org.jetbrains.annotations.NotNull;

public class HttpAudioResourceIntent extends AudioResourceIntent<HttpAudioResource> {

    private static final String EXTRA_PROPERTY = "EXTRA_PROPERTY";

    @NotNull
    @Override
    public Intent into(HttpAudioResource data, NotificationInfo notificationInfo, Intent intent) {
        intent.putExtra(EXTRA_PROPERTY, data.getExtraProperty());
        return super.into(data, notificationInfo, intent);
    }

    @NotNull
    @Override
    public HttpAudioResource get(Intent intent) {
        return new HttpAudioResource(
            intent.getStringExtra(HTTP_AUDIO_STREAM_URL),
            intent.getStringExtra(EXTRA_PROPERTY));
    }
}
