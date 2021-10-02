package com.example.testapplication.repository

import com.example.testapplication.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {

    fun searchForecast(query : String, appId : String) =  apiHelper.searchForecast(query, appId)

    fun getForecastByCoordinate(lat : String, lng : String, appId : String) = apiHelper.getForecastByCoordinate(lat,lng,appId)

}