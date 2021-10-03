package com.example.testapplication.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("app:visible")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("adapter")
fun setAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?) {
    if (adapter != null) {
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

    }
}

@BindingAdapter("isHorizontal")
fun isHorizontal(recyclerView: RecyclerView, isHorizontal: Boolean?) {
    if (isHorizontal == true)
        recyclerView.layoutManager = LinearLayoutManager(
            recyclerView.context,
            LinearLayoutManager.HORIZONTAL, false
        )
}