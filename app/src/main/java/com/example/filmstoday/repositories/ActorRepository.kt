package com.example.filmstoday.repositories

import androidx.lifecycle.MutableLiveData
import com.example.filmstoday.BuildConfig
import com.example.filmstoday.api.ApiService
import com.example.filmstoday.models.cast.ActorFullInfoModel
import com.example.filmstoday.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Response

class ActorRepository {

    private var apiService: ApiService = RetrofitInstance.api

    fun getActor(actorId: Int, _observer: MutableLiveData<ActorFullInfoModel>) {
        apiService.getActor(actorId, BuildConfig.MOVIES_API_KEY).enqueue(object :
            retrofit2.Callback<ActorFullInfoModel> {
            override fun onResponse(
                call: Call<ActorFullInfoModel>,
                response: Response<ActorFullInfoModel>
            ) {
                _observer.value = response.body()
            }

            override fun onFailure(call: Call<ActorFullInfoModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}