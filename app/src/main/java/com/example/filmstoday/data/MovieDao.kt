package com.example.filmstoday.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovieToWant(wantMovie: WantMovie)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovieToWatched(watchedMovie: WatchedMovie)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveComment(commentary: Commentary)

    @Query("SELECT * FROM want_table ORDER BY id ASC")
    fun readWantMovies(): LiveData<List<WantMovie>>

    @Query("SELECT * FROM watched_table ORDER BY id ASC")
    fun readWatchedMovies(): LiveData<List<WatchedMovie>>

    @Query("SELECT EXISTS (SELECT * FROM want_table WHERE movieId = :id)")
    suspend fun isMovieInWant(id: Int): Boolean

    @Query("SELECT EXISTS (SELECT * FROM watched_table WHERE movieId = :id)")
    suspend fun isMovieInWatched(id: Int): Boolean

    @Query("SELECT * FROM comments_table WHERE movieId = :movieId")
    fun getCommentary(movieId: Int): LiveData<Commentary>

}