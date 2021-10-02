package com.example.testapplication.utils

import android.text.format.DateUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

fun formatedDate() : String{
    val dateFormat = SimpleDateFormat("yyy d mmm")
    val date = Date()
    val fullDate = DateFormat.getDateInstance(DateFormat.FULL).format(date)
    val hours = dateFormat.format(date)

    return "$fullDate - $hours"
}

fun getNearestDate(){
    val now = System.currentTimeMillis()

    // Create a sample list of dates
    val dates: MutableList<Date> = ArrayList()

    val r = Random()
    for (i in 0..9) dates.add(Date(now + r.nextInt(10000) - 5000))

    // Get date closest to "now"
    val closest = Collections.min(dates) { d1, d2 ->
        val diff1 = abs(d1.time - now)
        val diff2 = abs(d2.time - now)
        diff1.compareTo(diff2)
    }
}

fun isToday(date : String):Boolean{
    val long = date.toFormatDate()?.toLong()
    return long?.let { DateUtils.isToday(it) } == true
}