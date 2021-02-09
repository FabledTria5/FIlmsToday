package com.example.filmstoday.models.cast

import com.google.gson.annotations.SerializedName

data class Actor(

    @SerializedName("profile_path") val profile_path : String?,
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,

)