package com.example.testapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.testapplication.core.BaseRecyclerAdapter
import com.example.testapplication.databinding.ViewSearchBinding
import com.example.testapplication.model.custom.WeatherInfo
import com.example.testapplication.utils.hideKeyboard


class SearchAdapter(private val callback : (WeatherInfo?) -> Unit) : BaseRecyclerAdapter<ViewSearchBinding>() {

    private var searchList : ArrayList<WeatherInfo> ?= arrayListOf()

    fun updateList(searchList : ArrayList<WeatherInfo>?){
        this.searchList = searchList
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = searchList?.get(position)
        holder.binding.root.setOnClickListener {
            it.hideKeyboard()
            callback(searchList?.get(position))
        }
    }

    override fun getItemCount(): Int {
        return searchList?.size ?: 0
    }


}