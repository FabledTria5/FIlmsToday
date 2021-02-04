package com.example.filmstoday.responses

import com.example.filmstoday.models.Movie

data class MoviesResponse(
    var page: Int,
    var results: List<Movie>
)