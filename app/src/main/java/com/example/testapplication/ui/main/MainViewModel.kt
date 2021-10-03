package com.example.testapplication.ui.main

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapplication.local.WeatherTable
import com.example.testapplication.model.custom.Location
import com.example.testapplication.model.custom.WeatherInfo
import com.example.testapplication.model.weather.Response
import com.example.testapplication.repository.MainRepository
import com.example.testapplication.ui.adapter.DailyForecastAdapter
import com.example.testapplication.ui.adapter.SearchAdapter
import com.example.testapplication.ui.adapter.TodayForecastAdapter
import com.example.testapplication.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import rx.Subscriber
import rx.Subscription
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel(){

    val forecast = MutableLiveData<WeatherInfo>()

    private val _error =  MutableLiveData<Resource<WeatherInfo>>()

    val error: LiveData<Resource<WeatherInfo>>
        get() = _error

    val progressVisibility = MutableLiveData(true)
    val searchProgressVisibility = MutableLiveData(true)
    val searchContentVisibility = MutableLiveData(false)

    val todayForecastAdapter  =  TodayForecastAdapter()
    val dailyForecastAdapter  =  DailyForecastAdapter()
    var searchAdapter :  SearchAdapter ?= null

    private var searchValue : Response? = null

    init {
        searchAdapter = SearchAdapter{
            // Item click listner
            searchValue?.let { it1 ->
                searchContentVisibility.value = false
                updateData(it1 , it)
            }
        }
    }

    fun getWeather(cityName : String = "London" , location : Location? = null) {
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
        } else _error.postValue(Resource.error("No internet connection", null))

    }

    private fun getSingleObserver(): SingleObserver<Response?>{
        return object : SingleObserver<Response?> {
            override fun onSubscribe(d: Disposable) {}
            override fun onSuccess(value: Response) {
                updateData(value)
            }
            override fun onError(e: Throwable) {
                progressVisibility.value = false
                _error.postValue(e.message?.let { Resource.error(it, null) })
            }
        }
    }

    fun loadLocalData(){
        val localObservable = mainRepository.getLocalForecast()

        localObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getLocalObserver())
    }

    private fun getLocalObserver(): SingleObserver<WeatherTable?>{
        return object : SingleObserver<WeatherTable?> {
            override fun onSubscribe(d: Disposable) {}
            override fun onSuccess(value: WeatherTable) {
                updateData(value)
            }
            override fun onError(e: Throwable) {
                progressVisibility.value = false
                //_error.postValue(e.message?.let { Resource.error(it, null) })
            }
        }
    }

    private fun updateData(value: Response , weatherInfo: WeatherInfo? = null){
        progressVisibility.value = false
        if(weatherInfo == null)
            forecast.value = value.list?.let { getFirstItem(it, value.city?.name) }
        else {
            weatherInfo.date = weatherInfo.date?.toFormatDate()
            forecast.value = weatherInfo
        }

        //store data in local database
        mainRepository.insertForecast(prepareWeatherToStore(value))

        val todayForecast = value.list?.let { getTodayForecast(it) }
        todayForecastAdapter.updateList(todayForecast)

        val dailyForecast = value.list?.let { getDailyForecast(it) }
        dailyForecastAdapter.updateList(dailyForecast)
    }

    private fun updateData(value: WeatherTable){
        progressVisibility.value = false

        forecast.value = value.list?.let { getFirstItem(it, value.cityName) }

        val todayForecast = value.list?.let { getTodayForecast(it) }
        todayForecastAdapter.updateList(todayForecast)

        val dailyForecast = value.list?.let { getDailyForecast(it) }
        dailyForecastAdapter.updateList(dailyForecast)
    }


    fun search(searchView: SearchView) {
        RxSearchObservable.fromView(searchView)
            .debounce(300, TimeUnit.MILLISECONDS)
            .doOnNext {
                searchContentVisibility.postValue( !it.isNullOrEmpty())
            }.filter { text ->
                text.isNotEmpty()
            }
            .distinctUntilChanged()
            .switchMapSingle {
                searchQuery(it)?.doOnError { it1 ->
                    it1.stackTrace
                }?.onErrorReturn { error ->
                    Response(null,null, error.message , null , null)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                if (result != null){
                    searchValue = result
                    searchAdapter?.updateList(getSearchList(result))
                }else
                    _error.postValue( Resource.error("No Founded Result", null))

                searchProgressVisibility.value = false
            }
    }

    private fun searchQuery(cityName : String) : Single<Response>?{
        return mainRepository.searchForecast(cityName, API_KEY)
    }



}
