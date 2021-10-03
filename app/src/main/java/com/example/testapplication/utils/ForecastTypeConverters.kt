package com.example.testapplication.utils

import androidx.room.TypeConverter
import com.example.testapplication.model.weather.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ForecastTypeConverters {
    @TypeConverter
    fun stringToMeasurements(json: String?): List<Item?>? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Item?>?>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun measurementsToString(list: List<Item?>?): String? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Item?>?>() {}.type
        return gson.toJson(list, type)
    }
}