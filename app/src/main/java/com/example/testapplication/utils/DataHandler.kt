package com.example.testapplication.utils

import com.example.testapplication.local.WeatherTable
import com.example.testapplication.model.custom.WeatherInfo
import com.example.testapplication.model.weather.Item
import com.example.testapplication.model.weather.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


fun getFirstItem(value: List<Item> , cityName : String?): WeatherInfo {
    val data = value[0]
    val date = data.dt_txt.toFormatDate()
    val item = WeatherInfo(0,
        cityName,
        data.main.temp.toCelsius().roundToInt().toString(),
        data.main.temp_min.toCelsius().roundToInt().toString(),
        data.main.temp_max.toCelsius().roundToInt().toString(),
        data.weather.get(0).description.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        },
        date)
    return item
}

fun prepareWeatherToStore(value: Response): WeatherTable {
    val data = value.list?.get(0)
    val date = data?.dt_txt?.toFormatDate()
    val item = WeatherTable(0,
        value.city?.name,
        data?.main?.temp?.toCelsius()?.roundToInt().toString(),
        data?.main?.temp_min?.toCelsius()?.roundToInt().toString(),
        data?.main?.temp_max?.toCelsius()?.roundToInt().toString(),
        data?.weather?.get(0)?.description?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        },
        date,value.list)
    return item
}

fun getSearchList(value: Response): ArrayList<WeatherInfo>{
    val list = ArrayList<WeatherInfo>()
    value.list?.forEach {
        val item = WeatherInfo(0 , value.city?.name
            ,it.main.temp.toCelsius().roundToInt().toString()
            ,it.main.temp_min.toCelsius().roundToInt().toString(),
            it.main.temp_max.toCelsius().roundToInt().toString(),
            it.weather[0].description.replaceFirstChar { it1 ->
                if (it1.isLowerCase()) it1.titlecase(
                    Locale.getDefault()
                ) else it1.toString()
            } ,
            it.dt_txt)
        list.add(item)
    }

    return list
}

fun getTodayForecast(value: List<Item>) : ArrayList<Item>{
    return value.filter {
        isToday(it.dt_txt)
    } as ArrayList<Item>
}

fun getDailyForecast(value: List<Item>) : ArrayList<Item>{
    val list  : ArrayList<Item> = arrayListOf()
    val dateList  : ArrayList<String> = arrayListOf()
    value.forEach {
        if (dateList.size == 0) {
            dateList.add(it.dt_txt.toFormatDate().toString())
            list.add(it)
        }else{
            if (dateList.contains(it.dt_txt.toFormatDate().toString()).not()) {
                dateList.add(it.dt_txt.toFormatDate().toString())
                list.add(it)
            }
        }
    }
    dateList.clear()
    return list
}
