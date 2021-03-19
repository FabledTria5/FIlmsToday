package com.example.filmstoday.adapters.listeners

import com.example.filmstoday.models.movie.SimpleMovie

interface OnSimpleMovieClickListener {
    fun onItemCLick(movie: SimpleMovie)
}