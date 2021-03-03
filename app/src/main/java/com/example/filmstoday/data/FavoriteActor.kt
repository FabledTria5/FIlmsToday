package com.example.filmstoday.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_actors")
data class FavoriteActor(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val actorId: Int,
    val actorPosterPath: String?,
    val actorName: String,
    val placeOfBirth: String?
)