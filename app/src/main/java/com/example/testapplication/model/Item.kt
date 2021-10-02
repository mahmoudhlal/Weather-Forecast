package com.example.testapplication.model

data class Item(
    val clouds: Clouds,
    val dt: Double,
    val dt_txt: String,
    val main: Main,
    val pop: Double,
    val rain: Rain,
    val sys: Sys,
    val visibility: Double,
    val weather: List<Weather>,
    val wind: Wind
)