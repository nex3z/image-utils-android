package com.nex3z.android.utils.image.util

class Timer {
    private val start: Long = System.currentTimeMillis()
    private var last: Long = -1

    fun stop(): Long {
        return System.currentTimeMillis() - start
    }

    fun delta(): Long {
        last = System.currentTimeMillis()
        return last - start
    }
}