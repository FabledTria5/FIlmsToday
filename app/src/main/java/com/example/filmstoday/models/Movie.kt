package com.example.filmstoday.models

data class Movie(
    var id: Int,
    var title: String,
    var overview: String,
    var poster_path: String?,
    var release_date: String
)