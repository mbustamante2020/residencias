package com.residencias.es.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.residencias.es.R
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.data.residence.Residence
import com.residencias.es.data.residence.Search
import com.residencias.es.utils.Status
import com.residencias.es.viewmodel.ResidencesViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


//class ResidencesMapsActivity : AppCompatActivity(), OnMapReadyCallback {

class ResidencesMapsFragment(private var search: Search?) : Fragment(R.layout.fragment_residences_maps), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val residencesViewModel: ResidencesViewModel by viewModel()
    private var residences: List<Residence>? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_residences_maps, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        lifecycleScope.launch {
            getResidences()
            observeResidences()

            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment!!.getMapAsync(this@ResidencesMapsFragment)

        }
    }

    private suspend fun getResidences() {
        try {
            residencesViewModel.getAllResidences(1, search)
        } catch (t: UnauthorizedException) {
            residencesViewModel.onUnauthorized()
        }
    }

    private fun observeResidences() {
        residencesViewModel.residences.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    residences = it.data?.second.orEmpty()
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
     /*   mMap = googleMap

        search?.let{
            Log.i("MainActivity Search", "${search?.search_for}")
            Log.i("MainActivity Search", "${search?.province}")
        }

        Log.i("MainActivity count", "${residences?.size}")


        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker ${residences?.size} Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        //zoom: 6,
        //center: new google.maps.LatLng(40.3, -3.74922),

        val iterator = residences?.iterator()
        iterator?.forEach { residence ->

            var latitude = residence?.latitude
            var longitude = residence?.longitude

            if (!longitude.isNullOrEmpty() && !latitude.isNullOrEmpty()) {
                val residenceMarker = LatLng(latitude.toDouble(), longitude.toDouble())
                mMap.addMarker(MarkerOptions().position(residenceMarker).title("${residence?.name}").snippet("${residence?.address}\n${residence?.phone}\nlll"))

            }
        }
        val residenceMarker = LatLng(40.3, -3.74922)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(residenceMarker, 6.0F))



    }

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_residences_maps)



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

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }*/
}