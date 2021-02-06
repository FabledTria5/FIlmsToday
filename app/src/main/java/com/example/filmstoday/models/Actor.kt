package com.example.filmstoday.models

import com.google.gson.annotations.SerializedName

data class Actor(

    @SerializedName("profile_path") val profile_path : String?,
    @SerializedName("adult") val adult : Boolean,
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("popularity") val popularity : Double

)