package com.example.filmstoday.responses

import com.example.filmstoday.models.cast.Actor
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ActorsResponse(

    @SerializedName("page") val page : Int,
    @SerializedName("results") val results : List<Actor>,
    @SerializedName("total_results") val total_results : Int,
    @SerializedName("total_pages") val total_pages : Int

) : Response(), Serializable
