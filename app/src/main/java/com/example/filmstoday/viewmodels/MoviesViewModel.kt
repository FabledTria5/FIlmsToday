package com.example.filmstoday.viewmodels

import androidx.lifecycle.*
import com.example.filmstoday.repositories.MoviesRepository
import com.example.filmstoday.responses.MoviesResponse

class MoviesViewModel : ViewModel(), LifecycleObserver {

    private val moviesRepository = MoviesRepository()
    private var currentPosition = 0

    private val _observingTabPosition = MutableLiveData<Int>()
    private val _observingMovies = MutableLiveData<MoviesResponse>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init() = changeTab(currentPosition)

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun orientationChanged() {
        _observingTabPosition.value = currentPosition
    }

    fun getObservedMovies() = _observingMovies
    fun getPosition() = _observingTabPosition

    fun changeTab(position: Int) {
        if (position == currentPosition && _observingMovies.value != null) return
        when (position) {
            0 -> moviesRepository.getPopularMovies(_observingMovies)
            1 -> moviesRepository.getNowPlayingMovies(_observingMovies)
            2 -> moviesRepository.getUpcomingMovies(_observingMovies)
            3 -> moviesRepository.getTopMovies(_observingMovies)
        }
        currentPosition = position
    }

}