package com.example.testapplication.model.weather

data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Double,
    val sunrise: Double,
    val sunset: Double,
    val timezone: Double
)