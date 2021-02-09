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
        fullMovieRepository.getMovieInfo(_observingMovie = _observingMovie, id = movieId)
        fullMovieRepository.getCast(_observingActors = _observingCast, id = movieId)
    }

    fun convertDate(date: String) = date.substring(0, 4)

    fun getCountry(productionCountries: List<ProductionCountries>): String {
        if (productionCountries.isEmpty()) return stringInteractor.textUnknown
        return productionCountries[0].name
    }

    fun getDescription(description: String?): String {
        if (description == null) return stringInteractor.textNoDescription
        return description
    }

    fun getDuration(runtime: Int?): String {
        if (runtime == null) return stringInteractor.textUnknown
        val hours = runtime / 60
        val minutes = runtime % 60
        return String.format("%dh %02dmin", hours, minutes)
    }
}