package com.example.filmstoday.viewmodels

import androidx.lifecycle.*
import com.example.filmstoday.repositories.MainActivityRepository

class MainActivityViewModel : ViewModel(), LifecycleObserver {
    fun getData() = MainActivityRepository.instance().getData()
}