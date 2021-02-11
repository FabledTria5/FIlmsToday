package com.example.filmstoday.models.movie

import com.google.gson.annotations.SerializedName

data class Genres(
    @SerializedName("name") val name : String
)