package com.example.testapplication.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapplication.model.custom.Location
import com.example.testapplication.model.custom.WeatherInfo
import com.example.testapplication.model.weather.Item
import com.example.testapplication.model.weather.Response
import com.example.testapplication.repository.MainRepository
import com.example.testapplication.ui.adapter.DailyForecastAdapter
import com.example.testapplication.ui.adapter.TodayForecastAdapter
import com.example.testapplication.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel(){

    val forecast = MutableLiveData<WeatherInfo>()

    /*val forecast: LiveData<Resource<WeatherInfo>>
        get() = _forecast*/

    val progressVisibility = MutableLiveData(true)

    val todayForecastAdapter  =  TodayForecastAdapter()
    val dailyForecastAdapter  =  DailyForecastAdapter()



    fun getWeather(cityName : String = "London" , location : Location? = null) {
        //forecast.postValue(Resource.loading(null))
        if (networkHelper.isNetworkConnected()) {
            val searchObservable =
                if (location == null)
                    mainRepository.searchForecast(cityName, API_KEY)
                else
                    mainRepository.getForecastByCoordinate(
                        location.lat.toString(),
                        location.lng.toString(),
                        API_KEY
                    )
            searchObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSingleObserver())
        } /*else forecast.postValue(Resource.error("No internet connection", null))*/

    }

    private fun getSingleObserver(): SingleObserver<Response?>{
        return object : SingleObserver<Response?> {
            override fun onSubscribe(d: Disposable) {
                Log.d("TAG", " onSubscribe : " + d.isDisposed)
            }

            override fun onSuccess(value: Response) {
                progressVisibility.value = false

                val data = value.list[0]
                val date = data.dt_txt.toFormatDate()
                val item = WeatherInfo(0 , value.city.name
                    , data.main.temp.toCelsius().roundToInt().toString()
                    , data.main.temp_min.toCelsius().roundToInt().toString()
                    , data.main.temp_max.toCelsius().roundToInt().toString()
                    , data.weather[0].description.capitalize()
                    , date)
                forecast.value = item

                val todayForecast = getTodayForecast(value)
                todayForecastAdapter.updateList(todayForecast)

                val dailyForecast = getDailyForecast(value)
                dailyForecastAdapter.updateList(dailyForecast)

            }

            override fun onError(e: Throwable) {
                progressVisibility.value = false
                //forecast.postValue(e.message?.let { Resource.error(it, null) })
            }
        }
    }

    private fun getTodayForecast(value: Response) : ArrayList<Item>{
        return value.list.filter {
            isToday(it.dt_txt)
        } as ArrayList<Item>
    }

    private fun getDailyForecast(value: Response) : ArrayList<Item>{
        val list  : ArrayList<Item> = arrayListOf()
        val dateList  : ArrayList<String> = arrayListOf()
        value.list.forEach {
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

}
