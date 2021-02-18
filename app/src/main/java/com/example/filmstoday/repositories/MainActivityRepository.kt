package com.example.filmstoday.repositories

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class MainActivityRepository {

    private val mediatorLiveData: MediatorLiveData<Boolean> = MediatorLiveData()

    companion object {
        private val INSTANCE = MainActivityRepository()
        fun instance() = INSTANCE
    }

    fun getData() = mediatorLiveData

    fun addDataSource(liveData: MutableLiveData<Boolean>) =
        mediatorLiveData.addSource(liveData, mediatorLiveData::setValue)

    fun removeDataSource(liveData: MutableLiveData<Boolean>) =
        mediatorLiveData.removeSource(liveData)
}