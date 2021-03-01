package com.example.filmstoday.models.movie

data class SimpleMovie(
    val movieId: Int,
    val posterPath: String,
    val movieTitle: String,
    val movieReleaseDate: String,
    val movieReleaseCountry: String,
    val movieRuntime: Int?,
    val movieRating: Double
)