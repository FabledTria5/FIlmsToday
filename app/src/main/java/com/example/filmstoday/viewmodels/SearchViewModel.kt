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

    private var currentMoviesQuery = ""
    private var currentActorsQuery = ""

    init {
        val movieDao = MoviesDatabase.getDatabase(application).movieDao()
        searchRepository = SearchRepository()
        movieRepository = MovieRepository(movieDao = movieDao)
    }

    fun getMovies(
        moviesCurrentPage: Int,
        query: String,
        searchAdultContent: Boolean,
    ): MutableLiveData<MoviesResponse> {
        return if (query != "") {
            currentMoviesQuery = query
            searchRepository.searchMovies(
                query = query,
                searchAdultContent = searchAdultContent,
                page = moviesCurrentPage
            )
        } else {
            searchRepository.searchMovies(
                query = currentMoviesQuery,
                searchAdultContent = searchAdultContent,
                page = moviesCurrentPage
            )
        }
    }


    fun getActors(actorsCurrentPage: Int, query: String): MutableLiveData<ActorsResponse> {
        return if (query != "") {
            currentActorsQuery = query
            searchRepository.searchActors(
                query = query,
                page = actorsCurrentPage
            )
        } else {
            searchRepository.searchActors(
                query = currentActorsQuery,
                page = actorsCurrentPage
            )
        }
    }
}