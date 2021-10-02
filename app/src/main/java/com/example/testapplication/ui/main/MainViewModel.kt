package com.example.testapplication.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapplication.core.BaseViewModel
import com.example.testapplication.model.Location
import com.example.testapplication.model.weather.Response
import com.example.testapplication.repository.MainRepository
import com.example.testapplication.utils.API_KEY
import com.example.testapplication.utils.NetworkHelper
import com.example.testapplication.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel(){

    private val _forecast = MutableLiveData<Resource<Response>>()

    val forecast: LiveData<Resource<Response>>
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
                _forecast.postValue(Resource.success(value))
            }

            override fun onError(e: Throwable) {
                _forecast.postValue(e.message?.let { Resource.error(it, null) })
            }
        }
    }

}
