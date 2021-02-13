package com.example.filmstoday.viewmodels

import androidx.lifecycle.*
import com.example.filmstoday.interactors.StringInteractor
import com.example.filmstoday.models.movie.MovieFullModel
import com.example.filmstoday.models.movie.ProductionCountries
import com.example.filmstoday.repositories.FullMovieRepository
import com.example.filmstoday.responses.CastResponse

class FullMovieViewModel(private val stringInteractor: StringInteractor) : ViewModel(),
    LifecycleObserver {

    private val _observingMovie = MutableLiveData<MovieFullModel>()
    private val _observingCast = MutableLiveData<CastResponse>()
    private val fullMovieRepository = FullMovieRepository()

    fun getObservedMovie() = _observingMovie
    fun getCast() = _observingCast

    fun getReceivedMovieInfo(movieId: Int) {
        fullMovieRepository.apply {
            getMovieInfo(_observingMovie = _observingMovie, id = movieId)
            getCast(_observingActors = _observingCast, id = movieId)
        }
    }

    fun convertDate(date: String) = date.substring(0, 4)

    fun getCountry(productionCountries: List<ProductionCountries>): String {
        return when (productionCountries.isEmpty()) {
            true -> stringInteractor.textUnknown
            else -> productionCountries[0].name
        }
    }

    fun getDescription(description: String?): String {
        description?.let { return description } ?: return stringInteractor.textNoDescription
    }

    fun getDuration(runtime: Int?): String {
        runtime?.let {
            val hours = it / 60
            val minutes = it % 60
            return String.format("%dh %02dmin", hours, minutes)
        } ?: return stringInteractor.textUnknown
    }
}