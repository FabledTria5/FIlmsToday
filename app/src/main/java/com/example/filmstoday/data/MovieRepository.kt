package com.example.filmstoday.data

import androidx.lifecycle.LiveData
import com.example.filmstoday.models.cast.ActorFullInfoModel
import com.example.filmstoday.models.movie.MovieFullModel
import com.example.filmstoday.utils.convertFullActorToFavorite
import com.example.filmstoday.utils.covertFullMovieToWant
import com.example.filmstoday.utils.covertFullMovieToWatched

class MovieRepository(private val movieDao: MovieDao) {

    val readWatchedMovies: LiveData<List<WatchedMovie>> = movieDao.readWatchedMovies()

    val readWantMovies: LiveData<List<WantMovie>> = movieDao.readWantMovies()

    val readFavoriteActors: LiveData<List<FavoriteActor>> = movieDao.readFavoriteActors()

    val readUserData: LiveData<User> = movieDao.readUserData()

    suspend fun saveUserData(user: User) = movieDao.saveUserData(user = user)

    suspend fun insertUser(user: User) = movieDao.addUser(user = user)

    suspend fun addMovieToWant(movieFullModel: MovieFullModel?) = movieFullModel?.let {
        movieDao.addMovieToWant(covertFullMovieToWant(movieFull = it))
    }

    suspend fun addMovieToWatched(movieFullModel: MovieFullModel?) = movieFullModel?.let {
        movieDao.addMovieToWatched(covertFullMovieToWatched(movieFull = it))
    }

    suspend fun isMovieInWant(id: Int) = movieDao.isMovieInWant(id = id)

    suspend fun isMovieInWatched(id: Int) = movieDao.isMovieInWatched(id = id)

    suspend fun saveActor(actorFullInfoModel: ActorFullInfoModel) =
        movieDao.saveActor(convertFullActorToFavorite(actorFullInfoModel = actorFullInfoModel))

    suspend fun removeActor(actorId: Int) = movieDao.removeActor(actorId = actorId)

    fun getFavorite(actorId: Int) = movieDao.getFavorite(actorId = actorId)
}