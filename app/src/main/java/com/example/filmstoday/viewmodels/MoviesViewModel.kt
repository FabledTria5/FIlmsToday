package com.example.filmstoday.viewmodels

import androidx.lifecycle.*
import com.example.filmstoday.repositories.MoviesRepository
import com.example.filmstoday.responses.MoviesResponse

class MoviesViewModel : ViewModel(), LifecycleObserver {

    private val moviesRepository = MoviesRepository()
    private var currentPosition = 0

    private val _observingTabPosition = MutableLiveData<Int>()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun orientationChanged() {
        _observingTabPosition.value = currentPosition
    }

    fun getObservedMovies(currentPage: Int, selectedTabPosition: Int) =
        changeSource(selectedTabPosition, currentPage)

    fun getPosition() = _observingTabPosition

    private fun changeSource(position: Int, currentPage: Int): LiveData<MoviesResponse> {
        currentPosition = position
        return when (position) {
            0 -> moviesRepository.getPopularMovies(currentPage)
            1 -> moviesRepository.getNowPlayingMovies(currentPage)
            2 -> moviesRepository.getUpcomingMovies(currentPage)
            3 -> moviesRepository.getTopMovies(currentPage)
            else -> return moviesRepository.getPopularMovies(currentPage)
        }
    }
}