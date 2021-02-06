package com.example.filmstoday.models

import com.google.gson.annotations.SerializedName

data class ActorFullInfoModel(
    @SerializedName("birthday") val birthday : String,
    @SerializedName("name") val name : String,
    @SerializedName("biography") val biography : String,
)