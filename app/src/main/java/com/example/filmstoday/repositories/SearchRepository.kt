package com.example.filmstoday.repositories

import androidx.lifecycle.MutableLiveData
import com.example.filmstoday.BuildConfig
import com.example.filmstoday.api.ApiService
import com.example.filmstoday.network.RetrofitInstance
import com.example.filmstoday.responses.ActorsResponse
import com.example.filmstoday.responses.MoviesResponse
import retrofit2.Call
import retrofit2.Response

class SearchRepository {

    private var apiService: ApiService = RetrofitInstance.api

    fun searchMovies(
        query: String,
        observer: MutableLiveData<MoviesResponse>,
        searchAdultContent: Boolean
    ) {
        apiService.searchMoviesByName(key = BuildConfig.MOVIES_API_KEY, query = query, searchAdultContent)
            .enqueue(object : retrofit2.Callback<MoviesResponse> {
                override fun onResponse(
                    call: Call<MoviesResponse>,
                    response: Response<MoviesResponse>
                ) {
                    observer.value = response.body()
                }

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) =
                    t.printStackTrace()
            })
    }

    fun searchActors(query: String, observer: MutableLiveData<ActorsResponse>) {
        apiService.searchActorsByName(key = BuildConfig.MOVIES_API_KEY, query = query)
            .enqueue(object : retrofit2.Callback<ActorsResponse> {
                override fun onResponse(
                    call: Call<ActorsResponse>,
                    response: Response<ActorsResponse>
                ) {
                    observer.value = response.body()
                }

                override fun onFailure(call: Call<ActorsResponse>, t: Throwable) =
                    t.printStackTrace()
            })
    }

}