package com.cono.gongam.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
}