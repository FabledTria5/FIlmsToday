package com.example.filmstoday.models.movie

import com.google.gson.annotations.SerializedName

data class MovieModel(
    @SerializedName("id") val id: Int,
    @SerializedName("poster_path") val poster_path: String?,
)