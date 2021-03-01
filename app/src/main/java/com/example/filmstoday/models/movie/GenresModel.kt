package com.example.filmstoday.models.movie

import com.google.gson.annotations.SerializedName

data class GenresModel(
    @SerializedName("name") val name : String
)