package com.example.testapplication.api

import com.example.testapplication.model.weather.Response
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override  fun searchForecast(query : String, appId : String): Single<Response>
                                                     = apiService.searchForecast(query,appId)

    override  fun getForecastByCoordinate(lat : String, lng : String, appId : String): Single<Response>
                                                     = apiService.getForecastByCoordinate(lat,lng,appId)


}
