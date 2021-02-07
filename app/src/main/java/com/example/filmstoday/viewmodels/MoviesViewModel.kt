package com.example.filmstoday.viewmodels

import androidx.lifecycle.*
import com.example.filmstoday.repositories.MoviesRepository
import com.example.filmstoday.responses.MoviesResponse

class MoviesViewModel : ViewModel(), LifecycleObserver {

    private var moviesRepository = MoviesRepository()
    private var _currentPosition = 0

    private var _observingTabPosition = MutableLiveData<Int>()
    private var _observingMovies = MutableLiveData<MoviesResponse>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init() = changeTab(_currentPosition)

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun orientationChanged() {
        _observingTabPosition.value = _currentPosition
    }

    fun getObservedMovies() = _observingMovies
    fun getPosition() = _observingTabPosition

    fun changeTab(position: Int) {
        if (position == _currentPosition && _observingMovies.value != null) return
        when (position) {
            0 -> moviesRepository.getPopularMovies(_observingMovies)
            1 -> moviesRepository.getNowPlayingMovies(_observingMovies)
            2 -> moviesRepository.getUpcomingMovies(_observingMovies)
            3 -> moviesRepository.getTopMovies(_observingMovies)
        }
        _currentPosition = position
    }

}