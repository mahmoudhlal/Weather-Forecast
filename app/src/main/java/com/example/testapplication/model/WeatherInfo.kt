package com.example.testapplication.model

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
}