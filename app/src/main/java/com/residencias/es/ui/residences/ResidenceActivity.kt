package com.residencias.es.ui.residences

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.residencias.es.R
import com.residencias.es.data.network.Endpoints
import com.residencias.es.data.residences.Residence
import com.residencias.es.databinding.ActivityResidenceBinding
import com.residencias.es.viewmodel.ResidenceViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class ResidenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResidenceBinding
    private var residence: Residence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResidenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent: Intent = intent
        residence = intent.getParcelableExtra("residence")

        binding.residenceName.text = "${residence?.name}"

        //se muestra el textView del price solo si se obtiene un valor mayor a cero
        var detail: String = if( !residence?.price.isNullOrEmpty() && residence?.price != "0" ) {
            "Prices desde: ".plus(residence?.price.toString()).plus(" €\n")
        } else {
            "Prices: ".plus("No disponibles  \n")
        }

        binding.price.text = detail

       // binding.detail.text = "${detail}"
        
        binding.description.text = "${residence?.description}"

        //se muestra la imagen principal de la Residence
        residence?.urlImagen?.let {
            Glide.with(this)
                    .load("${Endpoints.urlImagen}/${it}-240x160.webp")
                    .error(R.drawable.ic_no_available)
                    //.centerCrop()
                    .into(binding.image)

        } ?: run {
            Glide.with(this)
                    .load(R.drawable.ic_no_available)
                    .centerCrop()
                    .into(binding.image)
        }

        binding.btnMap.setOnClickListener {
            val intent = Intent(this, ResidenceMapsActivity::class.java)
            intent.putExtra("residence", residence)
            startActivity(intent)
        }
    }
}