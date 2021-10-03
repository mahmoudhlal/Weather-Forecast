package com.example.testapplication.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecast(vararg users: WeatherTable)

    @Query("SELECT * FROM weather_table")
    fun loadForecast(): Single<WeatherTable>


}