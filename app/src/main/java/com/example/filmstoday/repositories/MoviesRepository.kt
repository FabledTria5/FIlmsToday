package com.example.filmstoday.repositories

import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import com.example.filmstoday.BuildConfig
import com.example.filmstoday.api.ApiService
import com.example.filmstoday.network.RetrofitInstance
import com.example.filmstoday.responses.MoviesResponse
import retrofit2.Call
import retrofit2.Response

class MoviesRepository {

    private var apiService: ApiService = RetrofitInstance.api
    private val TAG = "MoviesRepository"

    fun getPopularMovies(observingMovies: MutableLiveData<MoviesResponse>) {

        apiService.getPopular(BuildConfig.MOVIES_API_KEY).enqueue(object :
            retrofit2.Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, @NonNull response: Response<MoviesResponse>) {
                observingMovies.value = response.body()
            }

            override fun onFailure(call: Call<MoviesResponse>, @NonNull t: Throwable) {

            }
        })
    }

    fun getNowPlayingMovies(observingMovies: MutableLiveData<MoviesResponse>) {
        apiService.getNowPlaying(BuildConfig.MOVIES_API_KEY).enqueue(object : retrofit2.Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, @NonNull response: Response<MoviesResponse>) {
                observingMovies.value = response.body()
            }
            override fun onFailure(call: Call<MoviesResponse>, @NonNull t: Throwable) {

            }
        })
    }

    fun getUpcomingMovies(observingMovies: MutableLiveData<MoviesResponse>){
        apiService.getUpcoming(BuildConfig.MOVIES_API_KEY).enqueue(object : retrofit2.Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, @NonNull response: Response<MoviesResponse>) {
                observingMovies.value = response.body()
            }
            override fun onFailure(call: Call<MoviesResponse>, @NonNull t: Throwable) {
            }
        })
    }

    fun getTopMovies(observingMovies: MutableLiveData<MoviesResponse>) {
        apiService.getTop(BuildConfig.MOVIES_API_KEY).enqueue(object : retrofit2.Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, @NonNull response: Response<MoviesResponse>) {
                observingMovies.value = response.body()
            }
            override fun onFailure(call: Call<MoviesResponse>, @NonNull t: Throwable) {

            }
        })
    }

}