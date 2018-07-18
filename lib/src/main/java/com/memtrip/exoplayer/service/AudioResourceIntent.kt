package com.memtrip.exoplayer.service

import android.content.Intent

interface AudioResourceIntent<T : AudioResource> {

    fun into(data: T, intent: Intent)

    fun get(intent: Intent): T
}