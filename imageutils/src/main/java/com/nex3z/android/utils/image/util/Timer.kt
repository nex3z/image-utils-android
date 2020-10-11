package com.nex3z.android.utils.image.util

class Timer {
    private val start: Long = System.currentTimeMillis()

    fun stop(): Long {
        return System.currentTimeMillis() - start
    }
}