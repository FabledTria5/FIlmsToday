package com.example.filmstoday.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.filmstoday.repositories.MoviesRepository
import com.example.filmstoday.responses.MoviesResponse

class MoviesViewModel: ViewModel() {

    private var moviesRepository = MoviesRepository()

    fun getPopularMovies(apiKey: String) : LiveData<MoviesResponse> {
        return moviesRepository.getPopularMovies(apiKey)
    }

}