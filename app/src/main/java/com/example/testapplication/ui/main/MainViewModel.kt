package com.example.testapplication.ui.main

import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapplication.core.BaseViewModel
import com.example.testapplication.model.Location
import com.example.testapplication.model.WeatherInfo
import com.example.testapplication.model.weather.Item
import com.example.testapplication.model.weather.Response
import com.example.testapplication.repository.MainRepository
import com.example.testapplication.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _forecast = MutableLiveData<Resource<WeatherInfo>>()

    val forecast: LiveData<Resource<WeatherInfo>>
        get() = _forecast


    fun getWeather(cityName : String = "London" , location : Location? = null) {
        _forecast.postValue(Resource.loading(null))
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
        } else _forecast.postValue(Resource.error("No internet connection", null))

    }

    private fun getSingleObserver(): SingleObserver<Response?>{
        return object : SingleObserver<Response?> {
            override fun onSubscribe(d: Disposable) {
                Log.d("TAG", " onSubscribe : " + d.isDisposed)
            }

            override fun onSuccess(value: Response) {
                val data = value.list[0]
                val date = data.dt_txt.toFormatDate()
                val item = WeatherInfo(0 , value.city.name
                    , data.main.temp.toCelsius().roundToInt().toString()
                    , data.main.temp_min.toCelsius().roundToInt().toString()
                    , data.main.temp_max.toCelsius().roundToInt().toString()
                    , data.weather[0].description.capitalize()
                    , date)

                _forecast.postValue(Resource.success(item))
            }

            override fun onError(e: Throwable) {
                _forecast.postValue(e.message?.let { Resource.error(it, null) })
            }
        }
    }

    private fun getTodayForecast(value: Response) : ArrayList<Item>{
        return value.list.filter {
            isToday(it.dt_txt)
        } as ArrayList<Item>
    }

}
