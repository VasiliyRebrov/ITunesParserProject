package com.itunesparser.components.adapters

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.data.common.ResponseStatusEnum
import com.data.model.Album
import com.itunesparser.components.Status


@BindingAdapter("app:update")
fun update(view: RecyclerView, albums: List<Album>?) {
    albums?.let { (view.adapter as RecyclerAlbumsAdapter).updateList(it) }
}


@BindingAdapter("android:text")
fun setText(view: TextView, responseStatus: ResponseStatusEnum?) {
    responseStatus?.let { view.text = it.title }
}

@BindingAdapter("app:hiding")
fun hiding(view: ProgressBar, status: Status) {
    view.visibility = if (status == Status.LOADING) View.VISIBLE else View.GONE
}
