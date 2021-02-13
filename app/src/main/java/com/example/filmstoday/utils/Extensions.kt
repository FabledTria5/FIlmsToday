package com.example.filmstoday.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(stringResource: Int, duration: Int) =
    Snackbar.make(this, resources.getString(stringResource), duration).show()

