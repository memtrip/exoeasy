package com.memtrip.exoeasy.player

internal data class PlayerConfig(
    internal val streamUrl: String,
    internal val userAgent: String,
    internal val trackProgress: Boolean
)