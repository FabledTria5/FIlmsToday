package com.example.filmstoday.interactors

import android.content.Context
import com.example.filmstoday.R

class StringInteractorImpl(private val context: Context): StringInteractor {
    override val textUnknown: String
        get() = context.getString(R.string.unknown)
    override val textNoDescription: String
        get() = context.getString(R.string.no_description)
    override val textNoComments: String
        get() = context.getString(R.string.no_comments)
}