package com.example.filmstoday.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmstoday.interactors.StringInteractor
import com.example.filmstoday.viewmodels.FullMovieViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class FullMovieViewModelFactory(
    private val stringInteractor: StringInteractor,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FullMovieViewModel::class.java)) {
            return FullMovieViewModel(
                stringInteractor = stringInteractor,
                application = application) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }

}