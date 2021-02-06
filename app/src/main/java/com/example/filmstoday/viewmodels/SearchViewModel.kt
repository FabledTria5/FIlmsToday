package com.example.filmstoday.viewmodels

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmstoday.repositories.SearchRepository
import com.example.filmstoday.responses.ActorsResponse
import com.example.filmstoday.responses.MoviesResponse

class SearchViewModel : ViewModel(), LifecycleObserver {

    private var searchRepository = SearchRepository()

    private var _observingMovies = MutableLiveData<MoviesResponse>()
    private var _observingActors = MutableLiveData<ActorsResponse>()

    fun getMovies() = _observingMovies
    fun getActors() = _observingActors

    fun textChanged(query: String?) {
        if (query == null) {
            _observingMovies.value = null
            _observingActors.value = null
        }

        searchRepository.searchMovies(query = query!!, _observingMovies)
        searchRepository.searchActors(query = query, _observingActors)
    }

}