package com.example.filmstoday.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.filmstoday.R
import com.example.filmstoday.models.movie.ProductionCountries

fun getDuration(runtime: Int?): String {
    runtime?.let {
        val hours = it / 60
        val minutes = it % 60
        return String.format("%dh %02dmin", hours, minutes)
    } ?: return "Unknown"
}

fun getCountry(countries: List<ProductionCountries>): String {
    if (countries.count() == 0) return "Unknown"
    return countries.first().name
}

fun selectText(textView: TextView, context: Context) =
    textView.setTextColor(ContextCompat.getColor(context, R.color.white))

fun unselectText(textView: TextView, context: Context) =
    textView.setTextColor(ContextCompat.getColor(context, R.color.actorBirthText))

fun observeFavoriteIcon(imageView: ImageView, favorite: Boolean) {
    imageView.apply {
        when {
            favorite -> setImageResource(R.drawable.ic_favorite)
            else -> setImageResource(R.drawable.ic_add_to_favorite)
        }
    }
}