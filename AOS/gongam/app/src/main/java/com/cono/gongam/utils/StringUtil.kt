package com.cono.gongam.utils

object StringUtil {
    fun convertToTwoDigitString(number: Int): String {
        return if (number in 0..9) {
            "0$number"
        } else {
            number.toString()
        }
    }
}