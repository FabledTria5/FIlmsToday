package com.example.filmstoday.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmstoday.interactors.StringInteractor
import com.example.filmstoday.viewmodels.ActorViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ActorViewModelFactory(
    private val stringInteractor: StringInteractor,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActorViewModel::class.java)) {
            return ActorViewModel(
                application = application,
                stringInteractor = stringInteractor
            ) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }

}