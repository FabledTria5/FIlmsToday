package com.example.filmstoday.responses

import com.example.filmstoday.models.movie.MovieModel

data class MoviesResponse(
    var page: Int,
    var results: List<MovieModel>
)