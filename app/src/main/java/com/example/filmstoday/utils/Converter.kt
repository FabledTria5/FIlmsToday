package com.example.filmstoday.utils

import com.example.filmstoday.data.WantMovie
import com.example.filmstoday.data.WatchedMovie
import com.example.filmstoday.models.movie.SimpleMovie

val movies = arrayListOf<SimpleMovie>()

fun convertWantToMovie(list: List<WantMovie>): List<SimpleMovie> {
    movies.clear()
    list.forEach {
        movies.add(
            SimpleMovie(
                it.id,
                it.posterPath,
                it.movieTitle,
                it.movieReleaseDate,
                it.movieReleaseCountry,
                it.movieRuntime,
                it.movieRating
            )
        )
    }
    return movies
}

fun convertWatchedToMovie(list: List<WatchedMovie>): List<SimpleMovie> {
    movies.clear()
    list.forEach {
        movies.add(
            SimpleMovie(
                it.id,
                it.posterPath,
                it.movieTitle,
                it.movieReleaseDate,
                it.movieReleaseCountry,
                it.movieRuntime,
                it.movieRating
            )
        )
    }
    return movies
}