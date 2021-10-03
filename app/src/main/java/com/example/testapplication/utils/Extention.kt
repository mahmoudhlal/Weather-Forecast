package com.example.testapplication.utils

import java.text.ParseException
import java.text.SimpleDateFormat

fun Double.toCelsius() : Double{
    return this - 273.15
}
fun String.toFormatDate() : String?{
    try {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = format.parse(this)
        return SimpleDateFormat("MMM d EEE").format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

fun String.toHour() : String?{
    try {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = format.parse(this)
        return SimpleDateFormat("hh:mm a").format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

fun String.toDayFormat() : String?{
    try {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = format.parse(this)
        return if (isToday(this)) "Today" else SimpleDateFormat("EEE").format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}



