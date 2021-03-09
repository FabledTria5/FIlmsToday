package com.example.filmstoday.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.filmstoday.data.MovieRepository
import com.example.filmstoday.data.MoviesDatabase
import com.example.filmstoday.models.cast.ActorFullInfoModel
import com.example.filmstoday.repositories.SearchRepository
import com.example.filmstoday.responses.ActorsResponse
import com.example.filmstoday.responses.MoviesResponse
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    private val searchRepository: SearchRepository
    private val movieRepository: MovieRepository

    private val _observingMovies = MutableLiveData<MoviesResponse>()
    private val _observingActors = MutableLiveData<ActorsResponse>()
    private val _observingActor = MutableLiveData<ActorFullInfoModel>()

    init {
        val movieDao = MoviesDatabase.getDatabase(application).movieDao()
        searchRepository = SearchRepository()
        movieRepository = MovieRepository(movieDao = movieDao)
    }

    fun getMovies() = _observingMovies
    fun getActors() = _observingActors
    fun getActor() = _observingActor

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

    fun searchActor(id: Int) = searchRepository.searchActor(id = id, observer = _observingActor)

    fun getFavorite(actorId: Int) = movieRepository.getFavorite(actorId = actorId)

    fun addActorToFavorite(actorFullInfoModel: ActorFullInfoModel) = viewModelScope.launch {
        movieRepository.saveActor(actorFullInfoModel = actorFullInfoModel)
    }

    fun removeActorFromFavorite(actorId: Int) = viewModelScope.launch {
        movieRepository.removeActor(actorId = actorId)
    }
}