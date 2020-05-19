package io.softwareshenanigans.catalyst.model

import android.os.Handler
import java.util.concurrent.TimeUnit

class Timer {
    private val handler = Handler()

    fun callMeAfter(duration: Int, target: Runnable) {
        handler.postDelayed(target, (duration * 1000).toLong())
    }

    fun dropAllPendingTasks() {
        handler.removeCallbacksAndMessages(null)
    }

    fun getWallClockTime(): Long {
        return System.nanoTime()
    }

    fun getIntervalMsecSince(wallClockTime: Long): Long {
        return TimeUnit.MILLISECONDS.convert(getWallClockTime() - wallClockTime, TimeUnit.NANOSECONDS)
    }
}