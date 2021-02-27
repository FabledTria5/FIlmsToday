package com.example.filmstoday.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watched_table")
data class WatchedMovie(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val movieId: Int,
    val posterPath: String,
    val movieTitle: String,
    val movieReleaseDate: String,
    val movieReleaseCountry: String,
    val movieRuntime: Int?,
    val movieRating: Double
)