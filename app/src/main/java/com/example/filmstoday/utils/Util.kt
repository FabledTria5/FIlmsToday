package com.example.filmstoday.utils

import android.content.Context
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