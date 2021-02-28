package com.example.filmstoday.data

import androidx.lifecycle.LiveData
import com.example.filmstoday.models.movie.MovieFullModel

class MovieRepository(private val movieDao: MovieDao) {

    val readWatchedMovies: LiveData<List<WatchedMovie>> = movieDao.readWatchedMovies()

    val readWantMovies: LiveData<List<WantMovie>> = movieDao.readWantMovies()

    suspend fun addMovieToWant(movieFullModel: MovieFullModel) =
        movieDao.addMovieToWant(covertToWantMovie(movieFull = movieFullModel))

    suspend fun addMovieToWatched(movieFullModel: MovieFullModel) =
        movieDao.addMovieToWatched(covertToWatchedMovie(movieFull = movieFullModel))

    suspend fun isMovieInWant(id: Int) = movieDao.isMovieInWant(id = id)

    suspend fun isMovieInWatched(id: Int) = movieDao.isMovieInWatched(id = id)

    fun getCommentary(id: Int) = movieDao.getCommentary(movieId = id)

    suspend fun saveComment(id: Int, text: String) =
        movieDao.saveComment(Commentary(0, movieId = id, text = text))

    private fun covertToWantMovie(movieFull: MovieFullModel) =
        WantMovie(
            0,
            movieId = movieFull.id,
            posterPath = movieFull.poster_path,
            movieTitle = movieFull.title,
            movieRuntime = movieFull.runtime,
            movieReleaseCountry = movieFull.production_countries.first().name,
            movieReleaseDate = movieFull.release_date,
            movieRating = movieFull.vote_average
        )

    private fun covertToWatchedMovie(movieFull: MovieFullModel) =
        WatchedMovie(
            0,
            movieId = movieFull.id,
            posterPath = movieFull.poster_path,
            movieTitle = movieFull.title,
            movieRuntime = movieFull.runtime,
            movieReleaseCountry = movieFull.production_countries.first().name,
            movieReleaseDate = movieFull.release_date,
            movieRating = movieFull.vote_average
        )

}