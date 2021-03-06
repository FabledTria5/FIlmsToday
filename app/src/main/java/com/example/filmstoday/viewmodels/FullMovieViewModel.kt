package com.example.filmstoday.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.filmstoday.data.MovieRepository
import com.example.filmstoday.data.MoviesDatabase
import com.example.filmstoday.models.movie.MovieFullModel
import com.example.filmstoday.models.videos.VideosBase
import com.example.filmstoday.repositories.FullMovieRepository
import com.example.filmstoday.responses.CastResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FullMovieViewModel(application: Application) :
    AndroidViewModel(application),
    LifecycleObserver {

    private val _observingMovie = MutableLiveData<MovieFullModel>()
    private val _observingCast = MutableLiveData<CastResponse>()
    private val _observingVideos = MutableLiveData<VideosBase>()

    private val fullMovieRepository: FullMovieRepository
    private val movieRepository: MovieRepository

    init {
        val movieDao = MoviesDatabase.getDatabase(application).movieDao()
        movieRepository = MovieRepository(movieDao = movieDao)
        fullMovieRepository = FullMovieRepository()
    }

    fun getObservedMovie() = _observingMovie
    fun getCast() = _observingCast
    fun getObservingVideos() = _observingVideos

    fun getReceivedMovieInfo(movieId: Int) = fullMovieRepository.apply {
        getMovieInfo(_observingMovie = _observingMovie, id = movieId)
        getCast(_observingActors = _observingCast, id = movieId)
        getVideos(movieId, _observingVideos)
    }

    fun observeWantBtn(id: Int) = movieRepository.isMovieInWant(id = id)

    fun observeWatchedBtn(id: Int) = movieRepository.isMovieInWatched(id = id)

    fun addMovieToWant(movieFullModel: MovieFullModel?) = viewModelScope.launch {
        movieRepository.addMovieToWant(movieFullModel = movieFullModel)
    }

    fun addMovieToWatched(movieFullModel: MovieFullModel?) = viewModelScope.launch {
        movieRepository.addMovieToWatched(movieFullModel = movieFullModel)
    }
}