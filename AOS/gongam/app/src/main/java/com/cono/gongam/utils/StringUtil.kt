package com.cono.gongam.utils

object StringUtil {
    fun convertToTwoDigitString(number: Int): String {
        return if (number in 0..9) {
            "0$number"
        } else {
            number.toString()
        }
    }

    fun convertToTwoDigitString(number: String): String {
        return when {
            number.isEmpty() -> "00"
            number.length == 1 -> "0$number"
            else -> number
        }
    }
}