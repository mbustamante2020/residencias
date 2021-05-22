package com.residencias.es.ui.map

import android.Manifest
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.residencias.es.R
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.data.residence.Residence
import com.residencias.es.data.residence.Search
import com.residencias.es.databinding.FragmentPhotosBinding
import com.residencias.es.databinding.FragmentResidencesMapsBinding
import com.residencias.es.utils.Status
import com.residencias.es.viewmodel.ResidencesViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


//class ResidencesMapsActivity : AppCompatActivity(), OnMapReadyCallback {

class ResidencesMapsFragment(private var search: Search?) : Fragment(R.layout.fragment_residences_maps),
    OnMapReadyCallback, GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private val residencesViewModel: ResidencesViewModel by viewModel()
    private var _binding: FragmentResidencesMapsBinding? = null
    private val binding get() = _binding!!

    private var residences: List<Residence>? = null
    private var myLocation: Location? = null

    private val REQUEST_PERMISSION_LOCATION = 101
    private var fusedLocationClient: FusedLocationProviderClient? = null


    private val DEFAULT_ZOOM = 7.5f

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentResidencesMapsBinding.inflate(inflater, container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_residences_maps, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        showProgress(true)

      //  lifecycleScope.launch {


            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment!!.getMapAsync(this@ResidencesMapsFragment)



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        //fusedLocationClient =  LocationServices.getFusedLocationProviderClient(this@ResidencesMapsFragment)
            getResidences()
            observeResidences()
        //}

        binding.btnCall.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${binding.btnCall.text}")))
            } catch (e: Exception){

            }
        }

        binding.btnWhatsapp.setOnClickListener {
            try {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Hello Swapz")
                    putExtra("jid", "${binding.btnCall.text}@s.whatsapp.net")
                    type = "text/plain"
                    setPackage("com.whatsapp")
                }
                startActivity(sendIntent)

            } catch (e: Exception){
                e.printStackTrace()
                val appPackageName = "com.whatsapp"
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                } catch (e :android.content.ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                }
            }
        }
    }

    private fun getResidences() {
        lifecycleScope.launch {
            try {
                residencesViewModel.getAllResidences(1, search)
            } catch (t: UnauthorizedException) {
                residencesViewModel.onUnauthorized()
            }
        }
    }

    private fun observeResidences() {
        residencesViewModel.residences.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    residences = it.data?.second.orEmpty()
                    loadResidence()
                    showProgress(false)
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        enableLocation()

        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener(this)
    }

    private fun enableLocation() {
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                Activity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION_LOCATION
            )
        } else {
            // Enable location button
            mMap.isMyLocationEnabled = true

            fusedLocationClient!!.lastLocation
                .addOnSuccessListener { location ->
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
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        
        var cameraLatitude = 0.0
        var cameraLongitude = 0.0
        var latitude: Double
        var longitude: Double

        val iterator = residences?.iterator()
        iterator?.forEach { residence ->

            latitude = residence.latitude?.toDouble() ?: 0.0
            longitude = residence.longitude?.toDouble() ?: 0.0

            cameraLatitude += latitude
            cameraLongitude += longitude


            if (latitude > 0) {
                val residenceMarker = LatLng(latitude, longitude)
                mMap.addMarker(MarkerOptions()
                    .position(residenceMarker)
                    .title("${residence.name}")
                    .snippet("${residence.address}, ${residence.province}\nTel: ${residence.phone}")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
            }
        }
        Log.i("camera", "$cameraLatitude, $cameraLongitude" )
        val residenceMarker = if( cameraLatitude > 0.0 ) {
            LatLng(cameraLatitude/(residences?.size ?: 1), cameraLongitude/(residences?.size ?: 1))
        } else {
            LatLng(40.3, -3.74922)
        }

        Log.i("camera", "${residenceMarker.latitude}, ${residenceMarker.longitude}" )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(residenceMarker, DEFAULT_ZOOM))

    }




    //////////////////////////////////////////////
    override fun onMarkerClick(marker : Marker): Boolean {
        // Markers have a z-index that is settable and gettable.
        marker.zIndex += 1.0f
        var distance = distanceTo(marker.position.latitude, marker.position.longitude) ?: ""

       // Toast.makeText(context, "${marker.title} se encuentra a $distance", Toast.LENGTH_LONG).show()

        binding.residenceDetails.visibility = View.VISIBLE
        binding.mapLayout.layoutParams.height = resources.getDimensionPixelSize(R.dimen.maps_height)

        val residenceDetail: List<String> = (marker.snippet).split("Tel: ")

        binding.residenceName.text = marker.title
        binding.residenceAddress.text = residenceDetail[0]
        binding.residenceDistance.text = resources.getText(R.string.label_distance).toString().plus(" $distance")

        if(residenceDetail.size > 1) {
            binding.btnCall.visibility = View.VISIBLE
            binding.btnWhatsapp.visibility = View.VISIBLE
            binding.btnCall.text = residenceDetail[1].trim()
        } else {
            binding.btnCall.visibility = View.INVISIBLE
            binding.btnWhatsapp.visibility = View.INVISIBLE
        }

        return false
    }

    override fun onMapClick(point: LatLng) {
        binding.residenceDetails.visibility = View.GONE
        binding.mapLayout.layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
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

    private fun showProgress(show: Boolean) {
        binding.progressBar!!.visibility = if (show) View.VISIBLE else View.GONE
    }
}