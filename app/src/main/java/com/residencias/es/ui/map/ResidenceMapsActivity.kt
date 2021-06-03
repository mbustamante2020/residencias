package com.residencias.es.ui.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.residencias.es.R
import com.residencias.es.data.residence.model.Residence

const val REQUEST_PERMISSION_LOCATION = 101
//const val DEFAULT_ZOOM = 12.0f
const val DEFAULT_ZOOM = 7.5f

class ResidenceMapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

     private lateinit var mMap: GoogleMap

     private var residence: Residence? = null
     private var myLocation: Location? = null


     private var fusedLocationClient: FusedLocationProviderClient? = null
     private var myProgressBarForMaps: ProgressBar? = null

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_residence_maps)
         // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         val mapFragment = supportFragmentManager
             .findFragmentById(R.id.map) as SupportMapFragment?
         mapFragment!!.getMapAsync(this)



         val intent: Intent = intent
         residence = intent.getParcelableExtra("residence")


         // Create an instance of GoogleAPIClient.
         fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
         myProgressBarForMaps = findViewById<View>(R.id.myProgressBarForMaps) as ProgressBar



         val actionBar = supportActionBar
         actionBar?.title = "${residence?.name}"
         actionBar?.setDisplayHomeAsUpEnabled(true)
         actionBar?.setDisplayHomeAsUpEnabled(true)
         //val colorDrawable = ColorDrawable(Color.parseColor("#0F9D58")
         actionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary, null)))

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

         enableLocation()
         loadResidence()

         mMap.setOnMarkerClickListener(this@ResidenceMapsActivity)
     }

    private fun enableLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION_LOCATION
            )
        } else {
            // Enable location button
            mMap.isMyLocationEnabled = true
            fusedLocationClient!!.lastLocation
                .addOnSuccessListener(this) { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        centerMapToUser(location)
                    }
                }
        }
    }

     private fun centerMapToUser(location: Location) {
         val latLng = LatLng(location.latitude, location.longitude)
         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM))
         myLocation = location
     }

     private fun loadResidence() {

         showProgress(true)
         mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
         mMap.uiSettings.isZoomControlsEnabled = true
         mMap.uiSettings.isMyLocationButtonEnabled = true

         //zoom: 6,
         //center: new google.maps.LatLng(40.3, -3.74922),


         val latitude: Double = residence?.latitude?.toDouble() ?: 0.0
         val longitude: Double = residence?.longitude?.toDouble() ?: 0.0

         if (latitude > 0) {
             val residenceMarker = LatLng(latitude, longitude)

             mMap.addMarker(MarkerOptions()
                 .position(residenceMarker)
                 .title("${residence?.name}")
                 .snippet("${residence?.address}, ${residence?.province}")
                 .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))

             mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(residenceMarker, DEFAULT_ZOOM))

         }
         showProgress(false)

     }



    override fun onMarkerClick(marker : Marker): Boolean {
        // Markers have a z-index that is settable and gettable.
        marker.zIndex += 1.0f
        val distance = distanceTo(marker.position.latitude, marker.position.longitude)

        Toast.makeText(this, "${marker.title} se encuentra a $distance",
            Toast.LENGTH_LONG).show()

        return false
    }

    private fun distanceTo(latitude: Double, longitude: Double): String {
        val residenceLoc = Location("")
        residenceLoc.latitude = latitude
        residenceLoc.longitude = longitude

        myLocation?.let {
            mMap.addPolyline(
                PolylineOptions()
                    .add(LatLng(it.latitude, it.longitude), LatLng(latitude, longitude))
                    .width(5f)
                    .color(Color.RED)
            )

            var distance: Float = residenceLoc.distanceTo(it)
            var dist = "$distance M"

            if (distance > 1000.0f) {
                distance /= 1000.0f
                dist = "$distance KM"
            }
            return dist
        }
        return ""
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showProgress(show: Boolean) {
        this.myProgressBarForMaps!!.visibility = if (show) View.VISIBLE else View.GONE
    }
}