package com.example.testapplication.model.weather

data class Response(
    val city: City?,
    val cnt: Double?,
    val cod: String?,
    val list: List<Item>?,
    val message: Double?
)