package com.example.filmstoday.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData

class NetworkReceiver : BroadcastReceiver() {

    private val data: MutableLiveData<Boolean> = MutableLiveData()

    fun getData() = data

    override fun onReceive(context: Context, intent: Intent) {
        data.value =
            intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
    }

}