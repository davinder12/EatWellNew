@file:Suppress("unused", "UNCHECKED_CAST")

package com.android.mealpass.widgets

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import mealpass.com.mealpass.R
import java.io.File

class AndroidUiBindingAdapters


@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("goneVisible")
fun hideShow(view: View, show: Boolean) {
    view.visibility = if (show) View.GONE else View.VISIBLE
}


@BindingAdapter("invisibleShow")
fun invisibleShow(view: View, show: Boolean) {
    view.visibility = if (show) View.INVISIBLE else View.VISIBLE
}


@BindingAdapter("app:listData")
fun <T> RecyclerView.listData(list: List<T>?) {
    (adapter as? ListAdapter<T, *>)?.submitList(list)
}

@BindingAdapter("app:src")
fun ImageView.setImageDrawable(resource: Drawable?) {
    resource?.let { this.setImageDrawable(it) }
}


@BindingAdapter("app:strike")
fun MaterialTextView.strike(text: String) {
    this.text = text
    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

@BindingAdapter("url")
fun ImageView.imageUrl(url: String?) {
    if (!url.isNullOrEmpty() && url.contains("http")) {
        Glide.with(this.context).load(url).placeholder(R.drawable.logo).fitCenter().centerCrop()
            .into(this)
    } else if (!url.isNullOrEmpty()) {
        Glide.with(this.context).load(File(url)).fitCenter().centerCrop().into(this)
    }
}

