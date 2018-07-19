package com.memtrip.exoplayer.service

import java.util.Locale

fun Long.secondsProgressFormat(): String {
    val hours = this / 3600
    val minutes = this % 3600 / 60
    val seconds = this % 60

    if (hours > 0) {
        return String.format(Locale.UK, "%2d:%02d:%02d", hours, minutes, seconds).trim()
    } else {
        return String.format(Locale.UK, "%02d:%02d", minutes, seconds).trim()
    }
}