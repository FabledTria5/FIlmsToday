package com.example.filmstoday.utils

fun getDuration(runtime: Int?): String {
    runtime?.let {
        val hours = it / 60
        val minutes = it % 60
        return String.format("%dh %02dmin", hours, minutes)
    } ?: return "Unknown"
}