package com.example.stopwatch

import java.text.SimpleDateFormat
import java.util.*

const val START_TIME = "00:00:00"
const val TIME_FORMAT = "HH:mm:ss"

fun String.toTimeInSeconds(): Int {
    val timeFormat = SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).apply { timeZone = SimpleTimeZone(0, "GMT") }
    return (timeFormat.parse(this).time / 1000).toInt()
}

fun Int.displayTime(): String {
    if (this <= 0L) {
        return START_TIME
    }

    val timeFormat = SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).apply { timeZone = SimpleTimeZone(0, "GMT") }
    return timeFormat.format(Date(this * 1000L))
}