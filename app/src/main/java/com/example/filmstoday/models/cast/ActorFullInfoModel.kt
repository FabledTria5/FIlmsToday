package com.example.filmstoday.models.cast

import com.google.gson.annotations.SerializedName

data class ActorFullInfoModel(
    @SerializedName(value = "birthday") val birthday: String?,
    @SerializedName(value = "name") val name: String,
    @SerializedName(value = "biography") val biography: String,
    @SerializedName(value = "place_of_birth") val placeOfBirth: String?,
    @SerializedName(value = "profile_path") val photo: String?
)