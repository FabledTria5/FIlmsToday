package com.example.filmstoday.adapters.listeners

import com.example.filmstoday.models.movie.Movie

interface OnMovieClickListener {
    fun onItemClick(movie: Movie)
}