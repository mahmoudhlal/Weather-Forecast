package com.example.testapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.testapplication.core.BaseRecyclerAdapter
import com.example.testapplication.databinding.ViewTodayForecastBinding
import com.example.testapplication.model.custom.DayForecast
import com.example.testapplication.model.weather.Item
import com.example.testapplication.utils.toCelsius
import com.example.testapplication.utils.toHour
import kotlin.math.roundToInt

class TodayForecastAdapter : BaseRecyclerAdapter<ViewTodayForecastBinding>() {

    private var todayForecastList : ArrayList<Item> ?= arrayListOf()

    fun updateList(todayForecastList : ArrayList<Item>?){
        this.todayForecastList = todayForecastList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewTodayForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val temp = "${todayForecastList?.get(position)?.main?.temp?.toCelsius()?.roundToInt()}℃"
        val item = DayForecast(temp, todayForecastList?.get(position)?.dt_txt?.toHour())
        holder.binding.item = item
    }

    override fun getItemCount(): Int {
        return todayForecastList?.size ?: 0
    }

}