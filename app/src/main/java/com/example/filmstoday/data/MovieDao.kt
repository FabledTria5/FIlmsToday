package com.example.filmstoday.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovieToWant(wantMovie: WantMovie)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovieToWatched(watchedMovie: WatchedMovie)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveComment(commentary: Commentary)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveActor(favoriteActor: FavoriteActor)

    @Query(value = "SELECT * FROM favorite_actors")
    fun readFavoriteActors(): LiveData<List<FavoriteActor>>

    @Query(value = "SELECT * FROM want_table ORDER BY id ASC")
    fun readWantMovies(): LiveData<List<WantMovie>>

    @Query(value = "SELECT * FROM watched_table ORDER BY id ASC")
    fun readWatchedMovies(): LiveData<List<WatchedMovie>>

    @Query(value = "SELECT EXISTS (SELECT * FROM want_table WHERE movieId = :id)")
    suspend fun isMovieInWant(id: Int): Boolean

    @Query(value = "SELECT EXISTS (SELECT * FROM watched_table WHERE movieId = :id)")
    suspend fun isMovieInWatched(id: Int): Boolean

    @Query(value = "SELECT * FROM comments_table WHERE movieId = :movieId")
    fun getCommentary(movieId: Int): LiveData<Commentary>

    @Query(value = "SELECT EXISTS (SELECT * FROM favorite_actors WHERE actorId = :actorId)")
    fun getFavorite(actorId: Int): LiveData<Boolean>

}