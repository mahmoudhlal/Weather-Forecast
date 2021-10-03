package com.example.testapplication.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.testapplication.model.weather.Item
import com.example.testapplication.utils.ForecastTypeConverters

@Entity(tableName = "weather_table")
@TypeConverters(ForecastTypeConverters::class)
class WeatherTable (
    @PrimaryKey(autoGenerate = false)
    var id: Int? = 0,
    @ColumnInfo(name = "city_name") val cityName: String?,
    @ColumnInfo(name = "temp") val temp: String?,
    @ColumnInfo(name = "temp_min") val tempMin: String?,
    @ColumnInfo(name = "temp_max") val tempMax: String?,
    @ColumnInfo(name = "weather_desc") val weatherDesc: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "forecast") val list: List<Item>?
)