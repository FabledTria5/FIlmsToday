package com.example.filmstoday.repositories

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.filmstoday.BuildConfig
import com.example.filmstoday.api.ApiService
import com.example.filmstoday.network.RetrofitInstance
import com.example.filmstoday.responses.MoviesResponse
import retrofit2.Call
import retrofit2.Response

class MoviesRepository {

    private var apiService: ApiService = RetrofitInstance.api

    fun getPopularMovies(
        currentPage: Int
    ): MutableLiveData<MoviesResponse> {
        val data = MutableLiveData<MoviesResponse>()
        apiService.getPopular(BuildConfig.MOVIES_API_KEY, page = currentPage).enqueue(object :
            retrofit2.Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                @NonNull response: Response<MoviesResponse>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<MoviesResponse>, @NonNull t: Throwable) =
                t.printStackTrace()
        })
        return data
    }

    fun getNowPlayingMovies(
        currentPage: Int
    ): LiveData<MoviesResponse> {
        val data = MutableLiveData<MoviesResponse>()
        apiService.getNowPlaying(BuildConfig.MOVIES_API_KEY, page = currentPage)
            .enqueue(object : retrofit2.Callback<MoviesResponse> {
                override fun onResponse(
                    call: Call<MoviesResponse>,
                    @NonNull response: Response<MoviesResponse>
                ) {
                    data.value = response.body()
                }

                override fun onFailure(call: Call<MoviesResponse>, @NonNull t: Throwable) =
                    t.printStackTrace()
            })
        return data
    }

    fun getUpcomingMovies(
        currentPage: Int
    ): MutableLiveData<MoviesResponse> {
        val data = MutableLiveData<MoviesResponse>()
        apiService.getUpcoming(BuildConfig.MOVIES_API_KEY, page = currentPage)
            .enqueue(object : retrofit2.Callback<MoviesResponse> {
                override fun onResponse(
                    call: Call<MoviesResponse>,
                    @NonNull response: Response<MoviesResponse>
                ) {
                    data.value = response.body()
                }

                override fun onFailure(call: Call<MoviesResponse>, @NonNull t: Throwable) =
                    t.printStackTrace()
            })
        return data
    }

    fun getTopMovies(
        currentPage: Int
    ): MutableLiveData<MoviesResponse> {
        val data = MutableLiveData<MoviesResponse>()
        apiService.getTop(BuildConfig.MOVIES_API_KEY, page = currentPage)
            .enqueue(object : retrofit2.Callback<MoviesResponse> {
                override fun onResponse(
                    call: Call<MoviesResponse>,
                    @NonNull response: Response<MoviesResponse>
                ) {
                    data.value = response.body()
                }

                override fun onFailure(call: Call<MoviesResponse>, @NonNull t: Throwable) =
                    t.printStackTrace()
            })
        return data
    }
}