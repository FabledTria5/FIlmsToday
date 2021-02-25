package com.example.filmstoday.viewmodels

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmstoday.models.cast.ActorFullInfoModel
import com.example.filmstoday.repositories.SearchRepository
import com.example.filmstoday.responses.ActorsResponse
import com.example.filmstoday.responses.MoviesResponse

class SearchViewModel : ViewModel(), LifecycleObserver {

    private var searchRepository = SearchRepository()

    private var _observingMovies = MutableLiveData<MoviesResponse>()
    private var _observingActors = MutableLiveData<ActorsResponse>()
    private var _observingActor = MutableLiveData<ActorFullInfoModel>()

    fun getMovies() = _observingMovies
    fun getActors() = _observingActors
    fun getActor() = _observingActor

    fun textChanged(query: String) {
        if (query == "") {
            return
        }
        searchRepository.searchMovies(query = query, observer = _observingMovies)
        searchRepository.searchActors(query = query, observer = _observingActors)
    }

    fun searchActor(id: Int) {
        searchRepository.searchActor(id = id, observer = _observingActor)
    }
}