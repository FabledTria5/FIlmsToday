package com.example.filmstoday.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.filmstoday.R
import com.example.filmstoday.utils.Constants
import com.squareup.picasso.Picasso

@BindingAdapter("app:url")
fun loadImage(imageView: ImageView, photo: String?) {
    Picasso.get().load("${Constants.POSTERS_BASE_URL}${photo}")
        .placeholder(R.drawable.photo_placeholder)
        .into(imageView)
}