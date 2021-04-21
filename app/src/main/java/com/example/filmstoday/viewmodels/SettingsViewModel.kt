package com.example.filmstoday.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.filmstoday.data.MovieRepository
import com.example.filmstoday.data.MoviesDatabase
import com.example.filmstoday.data.User
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val userData: LiveData<User>
    private val repository: MovieRepository

    init {
        val movieDao = MoviesDatabase.getDatabase(application).movieDao()
        repository = MovieRepository(movieDao = movieDao)
        userData = repository.readUserData
    }

    fun getUser() = userData

    fun saveUserData(user: User) = viewModelScope.launch {
        if (userData.value == null) repository.insertUser(user = user)
        else {
            user.id = userData.value!!.id
            repository.saveUserData(user = user)
        }
    }
}