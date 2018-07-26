package com.memtrip.exoeasy.player

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray

/**
 * Copyright 2013-present memtrip LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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