package com.memtrip.exoeasy

interface AudioResource {
    fun url(): String
    fun userAgent(): String
    fun trackProgress(): Boolean
}