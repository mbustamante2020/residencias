package com.residencias.es.ui.residences

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.residencias.es.R
import com.residencias.es.data.residences.Residence
import com.residencias.es.databinding.ActivityResidenceBinding
import com.residencias.es.viewmodel.ResidenceViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class ResidenceActivity : AppCompatActivity() {

    private val ResidenceViewModel: ResidenceViewModel by viewModel()

    private lateinit var binding: ActivityResidenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResidenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent: Intent = getIntent()
        var residence: Residence = intent.getParcelableExtra("Residence")!!

        binding.name.text = "${residence.name}"


        var caracteristicasResumen: String
        //se muestra el textView del price solo si se obtiene un valor mayor a cero
        if( !residence.price.isNullOrEmpty() && residence.price != "0" ) {
            caracteristicasResumen = "Prices desde: ".plus(residence.price.toString()).plus(" €\n")
        } else {
            caracteristicasResumen = "Prices: ".plus("No disponibles  \n")
        }

        binding.price.text = "${caracteristicasResumen}"

/*
        //se muestra el textView de sectors disponibles solo si se obtiene un valor mayor a cero
        if( !Residence.sectors.isNullOrEmpty() && Residence.sectors == "1" ) {
            caracteristicasResumen = caracteristicasResumen.plus("Sectors disponibles: Si\n")
        }

        caracteristicas.text = caracteristicasResumen.plus("Teléfono: ${Residence.telefono.toString()}\n")

        var descripcionResumen: String = "";
        if( !Residence.descripcionResumen.isNullOrEmpty() ) {
            descripcionResumen = "${Residence.descripcionResumen}\n"
        }

        descripcion.text = "${descripcionResumen}Ubicada en ${Residence.direccion.toString()}, ${Residence.provincia.toString()}, ${Residence.town.toString()}"*/

        binding.detail.text = "${caracteristicasResumen}"




        binding.description.text = "${residence.shortDescription}"

        //se muestra la imagen principal de la Residence
        residence.urlImagen?.let {
            Glide.with(this)
                    .load("https://Residencesysalud.es/img/Residence/${it}-240x160.webp")
                    .error(R.drawable.ic_no_available)
                    .centerCrop()
                    .into(binding.image)

        } ?: run {
            Glide.with(this)
                    .load(R.drawable.ic_no_available)
                    .centerCrop()
                    .into(binding.image)
        }



        binding.btnMap.setOnClickListener {
            val intent = Intent(this, ResidenceMapsActivity::class.java)
            startActivity(intent)
        }
    }
}