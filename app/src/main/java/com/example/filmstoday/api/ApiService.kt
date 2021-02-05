package com.example.filmstoday.api

import com.example.filmstoday.responses.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(value = "/3/movie/popular")
    fun getPopular(@Query("api_key") key: String): Call<MoviesResponse>

    @GET(value = "/3/movie/now_playing")
    fun getNowPlaying(@Query("api_key") key: String): Call<MoviesResponse>

    @GET(value = "/3/movie/upcoming")
    fun getUpcoming(@Query("api_key") key: String): Call<MoviesResponse>

    @GET(value = "/3/movie/top_rated")
    fun getTop(@Query("api_key") key: String): Call<MoviesResponse>

}