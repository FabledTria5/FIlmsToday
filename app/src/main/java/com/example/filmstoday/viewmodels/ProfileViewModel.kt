package com.example.filmstoday.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.filmstoday.data.*
import com.example.filmstoday.models.movie.SimpleMovie

class ProfileViewModel(application: Application) : AndroidViewModel(application),
    LifecycleObserver {

    fun deleteMovie(item: SimpleMovie) {
        // TODO: 02.03.2021 Implement room functional
    }

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