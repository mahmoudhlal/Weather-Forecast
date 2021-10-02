package com.example.testapplication.local

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

class WeatherTable (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "city_name") val cityName: String?,
    @ColumnInfo(name = "temp") val temp: String?,
    @ColumnInfo(name = "temp_min") val tempMin: String?,
    @ColumnInfo(name = "temp_max") val tempMax: String?,
    @ColumnInfo(name = "weather_desc") val weatherDesc: String?,
    @ColumnInfo(name = "date") val date: String?
)