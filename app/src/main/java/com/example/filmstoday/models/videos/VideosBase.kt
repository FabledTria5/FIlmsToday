package com.example.filmstoday.models.videos

import com.google.gson.annotations.SerializedName

data class VideosBase(
    @SerializedName("id") val id : Int,
    @SerializedName("results") val results : List<Results>
)