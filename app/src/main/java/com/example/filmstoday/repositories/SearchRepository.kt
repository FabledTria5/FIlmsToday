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
        searchAdultContent: Boolean,
        page: Int,
    ): MutableLiveData<MoviesResponse> {
        val data = MutableLiveData<MoviesResponse>()
        apiService.searchMoviesByName(key = BuildConfig.MOVIES_API_KEY,
            query = query,
            include_adult = searchAdultContent,
            page = page)
            .enqueue(object : retrofit2.Callback<MoviesResponse> {
                override fun onResponse(
                    call: Call<MoviesResponse>,
                    response: Response<MoviesResponse>,
                ) {
                    data.value = response.body()
                }

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) =
                    t.printStackTrace()
            })
        return data
    }

    fun searchActors(query: String, page: Int): MutableLiveData<ActorsResponse> {
        val data = MutableLiveData<ActorsResponse>()
        apiService.searchActorsByName(key = BuildConfig.MOVIES_API_KEY, query = query, page = page)
            .enqueue(object : retrofit2.Callback<ActorsResponse> {
                override fun onResponse(
                    call: Call<ActorsResponse>,
                    response: Response<ActorsResponse>,
                ) {
                    data.value = response.body()
                }

                override fun onFailure(call: Call<ActorsResponse>, t: Throwable) =
                    t.printStackTrace()
            })
        return data
    }
}