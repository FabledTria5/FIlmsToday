package com.example.filmstoday.utils

import android.view.View
import com.example.filmstoday.R
import com.example.filmstoday.databinding.ActorBottomSheetLayoutBinding
import com.example.filmstoday.databinding.FragmentFullMovieBinding
import com.example.filmstoday.models.cast.ActorFullInfoModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

fun View.showSnackBar(stringResource: Int, duration: Int) =
    Snackbar.make(this, resources.getString(stringResource), duration).show()

