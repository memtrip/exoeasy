package com.memtrip.sample_java;

import com.memtrip.exoeasy.AudioResource;
import com.memtrip.exoeasy.sample_java.BuildConfig;

import org.jetbrains.annotations.NotNull;

public class HttpAudioResource implements AudioResource {

    private final String url;
    private final String extraProperty;

    HttpAudioResource(String url,
                      String extraProperty) {
        this.url = url;
        this.extraProperty = extraProperty;
    }

    String getExtraProperty() {
        return extraProperty;
    }

    @Override
    public boolean getTrackProgress() {
        return true;
    }

    @NotNull
    @Override
    public String getUrl() {
        return url;
    }

    @NotNull
    @Override
    public String getUserAgent() {
        return BuildConfig.VERSION_NAME + "/" + BuildConfig.VERSION_CODE;
    }
}
