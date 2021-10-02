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

