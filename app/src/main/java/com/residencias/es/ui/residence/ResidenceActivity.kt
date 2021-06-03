package com.residencias.es.ui.residence

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.residencias.es.R
import com.residencias.es.data.network.Endpoints
import com.residencias.es.data.residence.model.Residence
import com.residencias.es.databinding.ActivityResidenceBinding
import com.residencias.es.ui.map.ResidenceMapsActivity


class ResidenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResidenceBinding
    private var residence: Residence? = null
    //private val myResidenceViewModel: MyResidenceViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResidenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = "Residencia"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary, null)))

        val intent: Intent = intent
        residence = intent.getParcelableExtra("residence")

        setResidence()
/*
        getResidence()
        initObservers()
*/
        initButton()

    }

    private fun setResidence(){
        binding.residenceName.text = "${residence?.name}"

        //se muestra el textView del price solo si se obtiene un valor mayor a cero
        binding.price.text = if( !residence?.price.isNullOrEmpty() && residence?.price != "0" ) {
            "Precios desde: ".plus(residence?.price.toString()).plus(" €")
        } else {
            "Precios: ".plus("No disponibles")
        }

        binding.detail.text = residence?.address.plus(", ").plus(residence?.town).plus(", ").plus(residence?.province)

        binding.description.text = Html.fromHtml("${residence?.description}", Html.FROM_HTML_MODE_LEGACY).trim()

        //se muestra la imagen principal de la Residence
        residence?.urlImagen?.let {
            binding.image.visibility = View.VISIBLE
            Glide.with(this)
                .load("${Endpoints.urlImagen}/${it}-240x160.webp")
                .error(R.drawable.ic_no_available)
                //.centerCrop()
                .into(binding.image)

        } ?: run {
            binding.image.visibility = View.INVISIBLE
        }
    }

    private fun initButton() {
        binding.btnMap.setOnClickListener {
            val mapIntent = Intent(this, ResidenceMapsActivity::class.java)
            mapIntent.putExtra("residence", residence)
            startActivity(mapIntent)
        }

        binding.btnCall.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${residence?.phone}"))
            startActivity(callIntent)
        }

        binding.btnWhatsapp.setOnClickListener {
            try {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Hello Swapz")
                    putExtra("jid", "${residence?.phone}@s.whatsapp.net")
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
/*
    private fun getResidence() {
        lifecycleScope.launch {
            try {
                myResidenceViewModel.getAllRooms()
                myResidenceViewModel.getAllSectors()
                myResidenceViewModel.getAllDependencies()
            } catch (t: UnauthorizedException) {

            }
        }
    }

    private fun initObservers() {
        observeRooms()
        observeDependencies()
        observeSectors()
    }

    private fun observeRooms() {
        myResidenceViewModel.rooms.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {

                    val rooms = it.data.orEmpty() as ArrayList<Room>

                    for (position in rooms.indices) {

                     //   rooms[position].idResidence?.let {
                            binding.detail.text = (binding.detail.text).toString().plus(rooms[position].room)
                     //   }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    //Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeSectors() {
        myResidenceViewModel.sectors.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {

                    val sectors = it.data.orEmpty() as ArrayList<Sector>

                    for (position in sectors.indices) {
                        sectors[position].idResidence?.let {
                            binding.detail.text = (binding.detail.text).toString().plus(sectors[position].sector)
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    //Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeDependencies() {
        myResidenceViewModel.dependencies.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {

                    val dependencies = it.data.orEmpty() as ArrayList<Dependence>

                    for (position in dependencies.indices) {

                        dependencies[position].idResidence?.let {
                            binding.detail.text = (binding.detail.text).toString().plus(dependencies[position].dependence)
                        }
                    }

                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    //Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}