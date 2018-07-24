package com.memtrip.exoeasy

import android.content.Intent

interface AudioResourceIntent<T : AudioResource> {

    fun into(data: T, intent: Intent): Intent

    fun get(intent: Intent): T
}