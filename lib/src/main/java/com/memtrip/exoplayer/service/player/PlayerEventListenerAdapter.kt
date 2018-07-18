package com.memtrip.exoplayer.service.player

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray

internal interface PlayerEventListenerAdapter : Player.EventListener {
    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) { }
    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) { }
    override fun onLoadingChanged(isLoading: Boolean) { }
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) { }
    override fun onRepeatModeChanged(repeatMode: Int) { }
    override fun onPlayerError(error: ExoPlaybackException) { }
    override fun onPositionDiscontinuity() { }
    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) { }
}