package com.example.testapplication.api

import com.example.testapplication.model.weather.Response
import io.reactivex.Single

interface ApiHelper {

    fun searchForecast(query : String, appId : String): Single<Response>

    fun getForecastByCoordinate(lat : String, lng : String, appId : String): Single<Response>


}