package com.example.testapplication.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager


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


fun View.hideKeyboard() {
    val imm: InputMethodManager =
        this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}


