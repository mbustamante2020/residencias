package com.residencias.es.ui.residence

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResidenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent: Intent = intent
        residence = intent.getParcelableExtra("residence")

        binding.residenceName.text = "${residence?.name}"

        //se muestra el textView del price solo si se obtiene un valor mayor a cero
        val detail: String = if( !residence?.price.isNullOrEmpty() && residence?.price != "0" ) {
            "Prices desde: ".plus(residence?.price.toString()).plus(" €\n")
        } else {
            "Prices: ".plus("No disponibles  \n")
        }

        binding.price.text = detail

       // binding.detail.text = "${detail}"
        
        binding.description.text = "${residence?.description}"

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

        binding.btnMap.setOnClickListener {
            val mapIntent = Intent(this, ResidenceMapsActivity::class.java)
            mapIntent.putExtra("residence", residence)
            startActivity(mapIntent)
        }
        binding.btnCall.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${residence?.phone}"))
            startActivity(callIntent)
        }
        binding.btnTelegram.setOnClickListener {

            try {
                val telegramIntent = Intent(Intent.ACTION_VIEW)
                telegramIntent.data = Uri.parse("https://telegram.me/+5551234")
                startActivity(telegramIntent)
            } catch (e: java.lang.Exception) {
                Log.i("Error", "$e")
                // show error message
            }


           /*

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://telegram.me/+UT_USER_ID_HERE"));
            final String appName = "org.telegram.messenger";
                    i.setPackage(appName);
            this.startActivity(i);


           try {
                val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/"))

                telegram.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                telegram.setPackage("org.telegram.messenger")
                startActivity(telegram)
            } catch (e: Exception) {
                Log.i("Error", "$e")
            }*/
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
}