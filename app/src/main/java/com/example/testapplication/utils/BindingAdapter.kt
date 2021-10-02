package com.example.testapplication.utils

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:visible")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}