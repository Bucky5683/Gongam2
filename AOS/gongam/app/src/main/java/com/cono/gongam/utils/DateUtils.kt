package com.cono.gongam.utils

import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

object DateUtils {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())

    fun getCurrentDate(): String {
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        val currentDate = Date()
//        return dateFormat.format(currentDate)

        return LocalDate.now().format(dateFormatter)
    }

    fun getStartOfWeek(): LocalDate {
        return LocalDate.now().with(DayOfWeek.SUNDAY).minusDays(7)
    }

    fun getEndOfWeek(): LocalDate {
        return LocalDate.now().with(DayOfWeek.SATURDAY)
    }

    fun getDayFromDateString(dateString: String): Int {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = dateFormat.parse(dateString)

            val calendar = Calendar.getInstance()
            date?.let {
                calendar.time = it
                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

                val dayOfWeekAbbreviation = when (dayOfWeek) {
                    Calendar.SUNDAY -> 0
                    Calendar.MONDAY -> 1
                    Calendar.TUESDAY -> 2
                    Calendar.WEDNESDAY -> 3
                    Calendar.THURSDAY -> 4
                    Calendar.FRIDAY -> 5
                    Calendar.SATURDAY -> 6
                    else -> -1
                }
                return dayOfWeekAbbreviation
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return -1
    }

    fun getDayStringFromInt(dayInt: Int): String {
        val dayString =
            when (dayInt) {
                0 -> "S"
                1 -> "M"
                2 -> "T"
                3 -> "W"
                4 -> "T"
                5 -> "F"
                6 -> "S"
                else -> ""
            }

        return dayString
    }
}