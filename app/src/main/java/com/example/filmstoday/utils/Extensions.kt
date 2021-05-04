package com.example.filmstoday.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout

fun AppBarLayout.show() {
    setExpanded(true)
}

fun AppCompatActivity.toast(message: String, gravity: Int = Gravity.TOP) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
        setGravity(gravity, 0, 60)
        show()
    }
}

