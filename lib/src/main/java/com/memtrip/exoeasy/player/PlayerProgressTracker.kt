package com.memtrip.exoeasy.player

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PlayerProgressTracker(
    private val trackProgress: Boolean,
    private val url: String,
    val context: Context,
    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
) {

    private var previousLoggedDuration: Long = -1L

    fun track(duration: Long) {
        if (trackProgress) {
            if (Math.abs(duration - previousLoggedDuration) > 8000 || previousLoggedDuration == -1L) {
                previousLoggedDuration = duration
                sharedPreferences.edit().putLong(PROGRESS_TRACKER_DURATION, duration).apply()
                sharedPreferences.edit().putString(PROGRESS_TRACKER_URL, url).apply()
            }
        }
    }

    fun currentProgress(url: String): Long {
        val lastTrackedUrl = sharedPreferences.getString("PROGRESS_TRACKER_URL", null)
        if (url == lastTrackedUrl) {
            return sharedPreferences.getLong(PROGRESS_TRACKER_DURATION, 0)
        } else {
            sharedPreferences.edit().clear().apply()
            return 0
        }
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        const val PROGRESS_TRACKER_DURATION: String = "PROGRESS_TRACKER_DURATION"
        const val PROGRESS_TRACKER_URL: String = "PROGRESS_TRACKER_URL"
    }
}