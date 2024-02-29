package com.cono.gongam.utils

object TimeUtils {
    fun convertSecondsToTimeInString(seconds: Int): String {
        val hours = String.format("%02d", seconds / 3600)
        val minutes = String.format("%02d", (seconds % 3600) / 60)
        val remainingSeconds = String.format("%02d", seconds % 60)

        return "$hours:$minutes:$remainingSeconds"
    }

    fun convertSecondsToTimeInTriple(seconds: Int): Triple<String, String, String> {
        val hours = String.format("%02d", seconds / 3600)
        val minutes = String.format("%02d", (seconds % 3600) / 60)
        val remainingSeconds = String.format("%02d", seconds % 60)
        return Triple(hours, minutes, remainingSeconds)
    }
}