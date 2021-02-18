package com.example.filmstoday.viewmodels

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmstoday.repositories.SearchRepository
import com.example.filmstoday.responses.MoviesResponse

class SearchViewModel : ViewModel(), LifecycleObserver {

    private var searchRepository = SearchRepository()

    private var _observingMovies = MutableLiveData<MoviesResponse>()

    fun getMovies() = _observingMovies

    fun textChanged(query: String) {
        if (query == "") {
            return
        }
        searchRepository.searchMovies(query = query, _observingMovies)
    }
}