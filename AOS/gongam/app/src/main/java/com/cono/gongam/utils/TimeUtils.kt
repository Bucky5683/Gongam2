package com.cono.gongam.utils

object TimeUtils {
    fun convertSecondsToTime(seconds: Int): String {
        val hours = String.format("%02d", seconds / 3600)
        val minutes = String.format("%02d", (seconds % 3600) / 60)
        val remainingSeconds = String.format("%02d", seconds % 60)

        return "$hours:$minutes:$remainingSeconds"
    }
}