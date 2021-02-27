package com.example.filmstoday.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.filmstoday.data.MovieRepository
import com.example.filmstoday.data.MoviesDatabase
import com.example.filmstoday.interactors.StringInteractor
import com.example.filmstoday.models.cast.ActorFullInfoModel
import com.example.filmstoday.models.movie.MovieFullModel
import com.example.filmstoday.repositories.FullMovieRepository
import com.example.filmstoday.responses.CastResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FullMovieViewModel(application: Application, private val stringInteractor: StringInteractor) :
    AndroidViewModel(application),
    LifecycleObserver {

    private val _observingMovie = MutableLiveData<MovieFullModel>()
    private val _observingCast = MutableLiveData<CastResponse>()
    private val _observingActor = MutableLiveData<ActorFullInfoModel>()
    private val fullMovieRepository = FullMovieRepository()
    private val movieRepository: MovieRepository

    init {
        val movieDao = MoviesDatabase.getDatabase(application).movieDao()
        movieRepository = MovieRepository(movieDao = movieDao)
    }

    fun getObservedMovie() = _observingMovie
    fun getCast() = _observingCast
    fun getObservingActor() = _observingActor

    fun getReceivedMovieInfo(movieId: Int) {
        fullMovieRepository.apply {
            getMovieInfo(_observingMovie = _observingMovie, id = movieId)
            getCast(_observingActors = _observingCast, id = movieId)
        }
    }

    fun addMovieToWant(movieFullModel: MovieFullModel) {
        viewModelScope.launch {
            movieRepository.addMovieToWant(movieFullModel = movieFullModel)
        }
    }

    fun addMovieToWatched(movieFullModel: MovieFullModel) {
        viewModelScope.launch {
            movieRepository.addMovieToWatched(movieFullModel = movieFullModel)
        }
    }

    fun checkWantBtn(id: Int): Boolean = runBlocking {
        movieRepository.isMovieInWant(id = id)
    }

    fun checkWatchedBtn(id: Int): Boolean = runBlocking {
        movieRepository.isMovieInWatched(id = id)
    }

    fun getDescription(description: String?): String {
        description?.let { return description } ?: return stringInteractor.textNoDescription
    }

    fun getActorInfo(actorId: Int) {
        fullMovieRepository.getActorInfo(actorId = actorId, observer = _observingActor)
    }
}