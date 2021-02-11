package com.example.filmstoday.adapters

import com.example.filmstoday.models.movie.Movie

interface OnItemViewClickListener {
    fun onItemClick(movie: Movie)
}