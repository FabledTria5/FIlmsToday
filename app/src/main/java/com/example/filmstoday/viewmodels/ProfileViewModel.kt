package com.example.filmstoday.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.example.filmstoday.data.*

class ProfileViewModel(application: Application) : AndroidViewModel(application),
    LifecycleObserver {

    var readWantMovies: LiveData<List<WantMovie>>
    var readWatchMovies: LiveData<List<WatchedMovie>>
    var readFavoriteActors: LiveData<List<FavoriteActor>>
    private val repository: MovieRepository

    init {
        val movieDao = MoviesDatabase.getDatabase(application).movieDao()
        repository = MovieRepository(movieDao = movieDao)
        readWantMovies = repository.readWantMovies
        readWatchMovies = repository.readWatchedMovies
        readFavoriteActors = repository.readFavoriteActors
    }
}