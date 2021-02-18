package com.example.filmstoday.service

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.filmstoday.BuildConfig
import com.example.filmstoday.responses.ActorsResponse
import com.example.filmstoday.utils.Constants.Companion.BASE_URL
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class ActorsService(name: String = "ActorsService") : IntentService(name) {

    private val broadcastIntent = Intent("DETAILS INTENT FILTER")

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        val actorName = intent.getStringExtra("ActorName")
        if (actorName == "") return
        loadData(actorName)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadData(actorName: String?) {
        try {
            val uri = URL("${BASE_URL}3/search/person?query=$actorName&api_key=${BuildConfig.MOVIES_API_KEY}")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.apply {
                    requestMethod = "GET"
                    readTimeout = 10000
                }
                val actorsResponse: ActorsResponse =
                    Gson().fromJson(
                        getLines(BufferedReader(InputStreamReader(urlConnection.inputStream))),
                        ActorsResponse::class.java
                    )
                onResponse(actorsResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader) =
        reader.lines().collect(Collectors.joining("\n"))


    private fun onResponse(actorsResponse: ActorsResponse) {
        broadcastIntent.putExtra("actorsResponse", actorsResponse)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }
}