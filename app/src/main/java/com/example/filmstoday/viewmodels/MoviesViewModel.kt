package com.example.filmstoday.viewmodels

import androidx.lifecycle.ViewModel

class MoviesViewModel : ViewModel() {

    var selectedPosition = 0

    fun selectItem(position: Int?) {
        if (position != null) {
            selectedPosition = position
        }
    }
}