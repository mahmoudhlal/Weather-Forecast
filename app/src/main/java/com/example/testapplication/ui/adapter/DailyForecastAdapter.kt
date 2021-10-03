package com.example.testapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.testapplication.core.BaseRecyclerAdapter
import com.example.testapplication.databinding.ViewDailyForecastBinding
import com.example.testapplication.model.custom.DayForecast
import com.example.testapplication.model.custom.WeatherInfo
import com.example.testapplication.model.weather.Item
import com.example.testapplication.utils.toCelsius
import com.example.testapplication.utils.toDayFormat
import com.example.testapplication.utils.toHour
import kotlin.math.roundToInt

class DailyForecastAdapter : BaseRecyclerAdapter<ViewDailyForecastBinding>() {

    private var todayForecastList : ArrayList<Item> ?= arrayListOf()

    fun updateList(todayForecastList : ArrayList<Item>?){
        this.todayForecastList = todayForecastList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewDailyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemPosition = todayForecastList?.get(position)
        val item = WeatherInfo(0 ,"", itemPosition?.main?.temp.toString(),
            itemPosition?.main?.temp_min?.toCelsius()?.roundToInt().toString(),
            itemPosition?.main?.temp_max?.toCelsius()?.roundToInt().toString(),
            itemPosition?.weather?.get(0)?.description?.capitalize()
            , todayForecastList?.get(position)?.dt_txt?.toDayFormat())
        holder.binding.item = item
    }

    override fun getItemCount(): Int {
        return todayForecastList?.size ?: 0
    }

}