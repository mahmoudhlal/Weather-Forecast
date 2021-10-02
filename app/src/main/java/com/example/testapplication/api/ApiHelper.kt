package com.example.testapplication.api

import com.example.testapplication.model.Response
import io.reactivex.Single
import io.reactivex.SingleObserver

interface ApiHelper {

    fun searchForecast(query : String, appId : String): Single<Response>

    fun getForecastByCoordinate(lat : String, lng : String, appId : String): Single<Response>


}