package com.example.filmstoday.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.filmstoday.data.MovieRepository
import com.example.filmstoday.data.MoviesDatabase
import com.example.filmstoday.data.WantMovie
import com.example.filmstoday.data.WatchedMovie

class ProfileViewModel(application: Application) : AndroidViewModel(application),
    LifecycleObserver {

    val readWantMovies: LiveData<List<WantMovie>>
    val readWatchMovies: LiveData<List<WatchedMovie>>
    private val repository: MovieRepository

    init {
        val movieDao = MoviesDatabase.getDatabase(application).movieDao()
        repository = MovieRepository(movieDao = movieDao)
        readWantMovies = repository.readWantMovies
        readWatchMovies = repository.readWatchedMovies
    }
}