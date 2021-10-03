package com.example.testapplication.model.custom

import com.example.testapplication.utils.toDayFormat

class WeatherInfo (
    var id: Int? = null,
    val cityName: String?,
    val temp: String?,
    val tempMin: String?,
    val tempMax: String?,
    val weatherDesc: String?,
    val date: String?
    ){
    fun mixTemp():String{
        return "$tempMax\u2103 / $tempMin\u2103"
    }
    fun mixTempWithOneDeg():String{
        return "$tempMax / $tempMin\u2103"
    }
    fun mixManyString():String{
        return "$temp\u2103 ${date?.toDayFormat()} - $cityName"
    }
     fun mixCityName():String{
        return "forecast $cityName"
    }

}