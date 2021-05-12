package com.residencias.es.ui.residences

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.residencias.es.R
import com.residencias.es.data.residences.Residence


class ResidenceMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var residence: Residence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_residence_maps)

        val intent: Intent = intent
        residence = intent.getParcelableExtra("residence")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        //zoom: 6,
        //center: new google.maps.LatLng(40.3, -3.74922),


        var latitude = residence?.latitude
        var longitude = residence?.longitude

        if( !longitude.isNullOrEmpty() && !latitude.isNullOrEmpty()  ) {
            val residenceMarker = LatLng(latitude.toDouble(), longitude.toDouble())
            mMap.addMarker(MarkerOptions().position(residenceMarker).title("${residence?.name}").snippet("${residence?.address}\n${residence?.phone}\nlll"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(residenceMarker, 16.0F))
        }
    }
}