package com.example.testapplication.api

import com.example.testapplication.model.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("forecast")
    fun searchForecast(@Query("q") query : String ,
                       @Query("appid") appId : String): Single<Response>


    @GET("forecast")
    fun getForecastByCoordinate(@Query("lat") lat : String ,
                                @Query("lon") lng : String ,
                                @Query("appid") appId : String): Single<Response>

}