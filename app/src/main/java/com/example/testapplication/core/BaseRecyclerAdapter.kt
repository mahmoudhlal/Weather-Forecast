package com.example.testapplication.core

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T : ViewDataBinding> : RecyclerView.Adapter<BaseRecyclerAdapter<T>.ViewHolder>() {

    inner class ViewHolder(itemView: T) : RecyclerView.ViewHolder(itemView.root) {
        var binding = itemView
    }
}