package com.example.filmstoday.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.example.filmstoday.data.MovieRepository
import com.example.filmstoday.data.MoviesDatabase
import com.example.filmstoday.repositories.SearchRepository
import com.example.filmstoday.responses.ActorsResponse
import com.example.filmstoday.responses.MoviesResponse

class SearchViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    private val searchRepository: SearchRepository
    private val movieRepository: MovieRepository

    private val _observingMovies = MutableLiveData<MoviesResponse>()
    private val _observingActors = MutableLiveData<ActorsResponse>()

    init {
        val movieDao = MoviesDatabase.getDatabase(application).movieDao()
        searchRepository = SearchRepository()
        movieRepository = MovieRepository(movieDao = movieDao)
    }

    fun getMovies() = _observingMovies
    fun getActors() = _observingActors

    fun textChanged(query: String, searchAdultContent: Boolean) {
        if (query == "") {
            return
        }
        searchRepository.searchMovies(
            query = query,
            observer = _observingMovies,
            searchAdultContent
        )
        searchRepository.searchActors(query = query, observer = _observingActors)
    }
}