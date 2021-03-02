package com.example.filmstoday.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.filmstoday.data.MovieRepository
import com.example.filmstoday.data.MoviesDatabase
import com.example.filmstoday.data.WantMovie
import com.example.filmstoday.data.WatchedMovie
import com.example.filmstoday.models.movie.SimpleMovie

class ProfileViewModel(application: Application) : AndroidViewModel(application),
    LifecycleObserver {

    fun deleteMovie(item: SimpleMovie) {
        // TODO: 02.03.2021 Implement room functional
    }

    var readWantMovies: LiveData<List<WantMovie>>
    var readWatchMovies: LiveData<List<WatchedMovie>>
    private val repository: MovieRepository

    init {
        val movieDao = MoviesDatabase.getDatabase(application).movieDao()
        repository = MovieRepository(movieDao = movieDao)
        readWantMovies = repository.readWantMovies
        readWatchMovies = repository.readWatchedMovies
    }
}