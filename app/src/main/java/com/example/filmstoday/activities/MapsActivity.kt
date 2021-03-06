package com.example.filmstoday.activities

import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.filmstoday.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val MAX_ADDRESSES = 5

    private val coder: Geocoder by lazy { Geocoder(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.apply {
            val location = getLocationFromAddress(intent.getStringExtra("ActorPlaceOfBirth"))
            addMarker(
                MarkerOptions()
                    .position(location)
                    .title(intent.getStringExtra("ActorName"))
            )
            moveCamera(CameraUpdateFactory.newLatLng(location))
        }
    }

    private fun getLocationFromAddress(addressStr: String?): LatLng {
        coder.getFromLocationName(addressStr, MAX_ADDRESSES).also {
            return LatLng(it[0].latitude, it[0].longitude)
        }
    }
}