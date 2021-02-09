package com.example.filmstoday.responses

import com.example.filmstoday.models.cast.Actor
import com.google.gson.annotations.SerializedName

data class CastResponse(
    @SerializedName("cast") val cast: List<Actor>
)