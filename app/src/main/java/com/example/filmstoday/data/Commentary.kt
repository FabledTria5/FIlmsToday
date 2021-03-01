package com.example.filmstoday.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments_table")
data class Commentary(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val movieId: Int,
    val text: String?
)