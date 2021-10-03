package com.example.testapplication.repository

import com.example.testapplication.api.ApiHelper
import com.example.testapplication.local.WeatherDao
import com.example.testapplication.local.WeatherTable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper,private val dao: WeatherDao) {

    fun searchForecast(query : String, appId : String) =  apiHelper.searchForecast(query, appId)

    fun getForecastByCoordinate(lat : String, lng : String, appId : String) = apiHelper.getForecastByCoordinate(lat,lng,appId)

    fun getLocalForecast() = dao.loadForecast()

    fun insertForecast(weatherTable: WeatherTable) = Observable.just(dao).subscribeOn(Schedulers.io())
        .subscribe { it.insertForecast(weatherTable)  }


}