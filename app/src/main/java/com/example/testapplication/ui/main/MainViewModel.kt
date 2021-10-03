package com.example.testapplication.ui.main

import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapplication.model.custom.Location
import com.example.testapplication.model.custom.WeatherInfo
import com.example.testapplication.model.weather.Item
import com.example.testapplication.model.weather.Response
import com.example.testapplication.repository.MainRepository
import com.example.testapplication.ui.adapter.DailyForecastAdapter
import com.example.testapplication.ui.adapter.SearchAdapter
import com.example.testapplication.ui.adapter.TodayForecastAdapter
import com.example.testapplication.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList
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
    val searchProgressVisibility = MutableLiveData(true)
    val searchContentVisibility = MutableLiveData(false)

    val todayForecastAdapter  =  TodayForecastAdapter()
    val dailyForecastAdapter  =  DailyForecastAdapter()
    var searchAdapter :  SearchAdapter ?= null

    private var searchValue : Response? = null

    init {
        searchAdapter = SearchAdapter{
            searchValue?.let { it1 ->
                searchContentVisibility.value = false
                updateData(it1)
            }
        }
    }

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
            override fun onSubscribe(d: Disposable) {}

            override fun onSuccess(value: Response) {
                updateData(value)
            }

            override fun onError(e: Throwable) {
                progressVisibility.value = false
                //forecast.postValue(e.message?.let { Resource.error(it, null) })
            }
        }
    }

    private fun updateData(value: Response){
        progressVisibility.value = false

        val data = value.list?.get(0)
        val date = data?.dt_txt?.toFormatDate()
        val item = WeatherInfo(0 , value.city?.name
            , data?.main?.temp?.toCelsius()?.roundToInt().toString()
            , data?.main?.temp_min?.toCelsius()?.roundToInt().toString()
            , data?.main?.temp_max?.toCelsius()?.roundToInt().toString()
            , data?.weather?.get(0)?.description?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            , date)
        forecast.value = item

        val todayForecast = getTodayForecast(value)
        todayForecastAdapter.updateList(todayForecast)

        val dailyForecast = getDailyForecast(value)
        dailyForecastAdapter.updateList(dailyForecast)
    }


    fun search(searchView: SearchView) {
        RxSearchObservable.fromView(searchView)
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter { text ->
                text.isNotEmpty()
            }
            .distinctUntilChanged()
            .switchMapSingle {
                searchQuery(it)?.doOnError {
                    Log.d("SEARCH" , it.message.toString())
                }?.onErrorReturn { error ->
                    Response(null,null, error.message , null , null)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                searchContentVisibility.value = true
                searchProgressVisibility.value = false
                if (result != null){
                    searchValue = result
                    searchAdapter?.updateList(getSearchList(result))
                }
            }
    }


    fun closeSearch(){
        searchContentVisibility.value = false
    }


    //// Handle data
    private fun searchQuery(cityName : String) : Single<Response>?{
        //forecast.postValue(Resource.loading(null))
        if (networkHelper.isNetworkConnected()) {
            return mainRepository.searchForecast(cityName, API_KEY)
        } /*else forecast.postValue(Resource.error("No internet connection", null))*/
        return null
    }

    private fun getSearchList(value: Response): ArrayList<WeatherInfo>{
        val list = ArrayList<WeatherInfo>()
        value.list?.forEach {
            val item = WeatherInfo(0 , value.city?.name
                , it.main.temp.toCelsius().roundToInt().toString()
                , ""
                , ""
                , ""
                ,  it.dt_txt)
            list.add(item)
        }
        return list
    }

    private fun getTodayForecast(value: Response) : ArrayList<Item>{
        return value.list?.filter {
            isToday(it.dt_txt)
        } as ArrayList<Item>
    }

    private fun getDailyForecast(value: Response) : ArrayList<Item>{
        val list  : ArrayList<Item> = arrayListOf()
        val dateList  : ArrayList<String> = arrayListOf()
        value.list?.forEach {
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
