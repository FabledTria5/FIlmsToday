package com.example.filmstoday.adapters.listeners

import com.example.filmstoday.models.movie.MovieModel

interface OnMovieClickListener {
    fun onItemClick(movieModel: MovieModel)
}