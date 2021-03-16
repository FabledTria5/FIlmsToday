package com.example.filmstoday.models.videos

import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("id") val id : String,
    @SerializedName("key") val key : String,
    @SerializedName("site") val site : String,
)
