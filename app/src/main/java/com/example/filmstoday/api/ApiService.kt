package com.example.filmstoday.api

import com.example.filmstoday.models.cast.ActorFullInfoModel
import com.example.filmstoday.models.movie.MovieFullModel
import com.example.filmstoday.models.videos.VideosBase
import com.example.filmstoday.responses.ActorsResponse
import com.example.filmstoday.responses.CastResponse
import com.example.filmstoday.responses.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(value = "/3/movie/popular")
    fun getPopular(
        @Query("api_key") key: String,
        @Query(value = "page") page: Int
    ): Call<MoviesResponse>

    @GET(value = "/3/movie/now_playing")
    fun getNowPlaying(
        @Query("api_key") key: String,
        @Query(value = "page") page: Int
    ): Call<MoviesResponse>

    @GET(value = "/3/movie/upcoming")
    fun getUpcoming(
        @Query("api_key") key: String,
        @Query(value = "page") page: Int
    ): Call<MoviesResponse>

    @GET(value = "/3/movie/top_rated")
    fun getTop(
        @Query("api_key") key: String,
        @Query(value = "page") page: Int
    ): Call<MoviesResponse>

    @GET(value = "/3/search/movie")
    fun searchMoviesByName(
        @Query("api_key") key: String,
        @Query("query") query: String,
        @Query("include_adult") include_adult: Boolean
    ): Call<MoviesResponse>

    @GET(value = "/3/search/person")
    fun searchActorsByName(
        @Query("api_key") key: String,
        @Query("query") query: String
    ): Call<ActorsResponse>

    @GET(value = "/3/movie/{id}")
    fun getMovieDetails(
        @Path("id") id: Int,
        @Query("api_key") query: String
    ): Call<MovieFullModel>

    @GET(value = "/3/movie/{movie_id}/credits")
    fun getMovieCast(
        @Path("movie_id") id: Int,
        @Query("api_key") query: String
    ): Call<CastResponse>

    @GET(value = "/3/person/{person_id}")
    fun getActor(
        @Path("person_id") id: Int,
        @Query("api_key") query: String
    ): Call<ActorFullInfoModel>

    @GET(value = "/3/movie/{movie_id}/videos")
    fun getVideos(
        @Path(value = "movie_id") id: Int,
        @Query("api_key") query: String
    ): Call<VideosBase>

}