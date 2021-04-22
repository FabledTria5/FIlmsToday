package com.example.filmstoday.repositories

import androidx.lifecycle.MutableLiveData
import com.example.filmstoday.BuildConfig
import com.example.filmstoday.api.ApiService
import com.example.filmstoday.models.movie.MovieFullModel
import com.example.filmstoday.models.videos.VideosBase
import com.example.filmstoday.network.RetrofitInstance
import com.example.filmstoday.responses.CastResponse
import retrofit2.Call
import retrofit2.Response

class FullMovieRepository {
    private var apiService: ApiService = RetrofitInstance.api

    fun getMovieInfo(_observingMovie: MutableLiveData<MovieFullModel>, id: Int) {
        apiService.getMovieDetails(id = id, BuildConfig.MOVIES_API_KEY).enqueue(object :
            retrofit2.Callback<MovieFullModel> {
            override fun onResponse(
                call: Call<MovieFullModel>,
                response: Response<MovieFullModel>
            ) {
                _observingMovie.value = response.body()
            }

            override fun onFailure(call: Call<MovieFullModel>, t: Throwable) = t.printStackTrace()
        })
    }

    fun getCast(_observingActors: MutableLiveData<CastResponse>, id: Int) {
        apiService.getMovieCast(id = id, BuildConfig.MOVIES_API_KEY)
            .enqueue(object : retrofit2.Callback<CastResponse> {
                override fun onResponse(
                    call: Call<CastResponse>,
                    response: Response<CastResponse>
                ) {
                    _observingActors.value = response.body()
                }

                override fun onFailure(call: Call<CastResponse>, t: Throwable) = t.printStackTrace()
            })
    }

    fun getVideos(movieId: Int, _observingVideos: MutableLiveData<VideosBase>) {
        apiService.getVideos(id = movieId, BuildConfig.MOVIES_API_KEY)
            .enqueue(object : retrofit2.Callback<VideosBase> {
                override fun onResponse(call: Call<VideosBase>, response: Response<VideosBase>) {
                    _observingVideos.value = response.body()
                }

                override fun onFailure(call: Call<VideosBase>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
}