package com.example.filmstoday.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.filmstoday.data.MovieRepository
import com.example.filmstoday.data.MoviesDatabase
import com.example.filmstoday.interactors.StringInteractor
import com.example.filmstoday.models.cast.ActorFullInfoModel
import com.example.filmstoday.repositories.ActorRepository

class ActorViewModel(application: Application, private val stringInteractor: StringInteractor) :
    AndroidViewModel(application), LifecycleObserver {

    private val _observingActor = MutableLiveData<ActorFullInfoModel>()

    private val movieRepository: MovieRepository
    private val actorRepository: ActorRepository

    init {
        val movieDao = MoviesDatabase.getDatabase(application).movieDao()
        movieRepository = MovieRepository(movieDao = movieDao)
        actorRepository = ActorRepository()
    }

    fun getObservedActor() = _observingActor

    fun getActorInfo(actorId: Int) = actorRepository.getActor(actorId, _observingActor)

    fun getFavorite(actorId: Int) = movieRepository.getFavorite(actorId = actorId)

    fun triggerFavorite(actorId: Int) = movieRepository.triggerFavoriteActor(actorId)
}