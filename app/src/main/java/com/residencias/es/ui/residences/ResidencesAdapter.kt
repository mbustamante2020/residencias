package com.residencias.es.ui.residences

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.residencias.es.R
import com.residencias.es.data.network.Endpoints
import com.residencias.es.data.residences.Residence
import com.residencias.es.databinding.ItemResidenceBinding
import com.residencias.es.viewmodel.ResidencesViewModel


class ResidencesAdapter(private val viewModel: ResidencesViewModel) :
        ListAdapter<Residence, ResidencesAdapter.ResidenceViewHolder>(residencesDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResidenceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemResidenceBinding.inflate(inflater)
        return ResidenceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResidenceViewHolder, position: Int) {
        holder.bindTo(getItem(position), viewModel)
    }

    class ResidenceViewHolder(private val binding: ItemResidenceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(residence: Residence, viewModel: ResidencesViewModel) {
            with(binding) {

                //evento que permite acceder a la ficha de la residence
                itemResidence.setOnClickListener {
                    viewModel.residenceClicked(residence)
                }

                nombre.text = residence.name.toString()

                var caracteristicasResumen: String
                //se muestra el textView del price solo si se obtiene un valor mayor a cero
                if( !residence.price.isNullOrEmpty() && residence.price != "0" ) {
                    caracteristicasResumen = "Prices desde: ".plus(residence.price.toString()).plus(" €\n")
                } else {
                    caracteristicasResumen = "Prices: ".plus("No disponibles  \n")
                }

                //se muestra el textView de sectors disponibles solo si se obtiene un valor mayor a cero
                if( !residence.sectors.isNullOrEmpty() && residence.sectors == "1" ) {
                    caracteristicasResumen = caracteristicasResumen.plus("Sectors disponibles: Si\n")
                }

                caracteristicas.text = caracteristicasResumen.plus("Teléfono: ${residence.phone.toString()}\n")

                var descripcionResumen: String = "";
                if( !residence.shortDescription.isNullOrEmpty() ) {
                    descripcionResumen = "${residence.shortDescription}\n"
                }

                descripcion.text = "${descripcionResumen}Ubicada en ${residence.address.toString()}, ${residence.province.toString()}, ${residence.town.toString()}"

                //se muestra la imagen principal de la residence
                residence.urlImagen?.let {
                    Glide.with(itemView)
                        .load( "${Endpoints.imagenUrl}/${it}-240x160.webp")
                        .error(R.drawable.ic_no_available)
                        .centerCrop()
                        .into(imagenResidence)

                } ?: run {
                    Glide.with(itemView)
                        .load(R.drawable.ic_no_available)
                        .centerCrop()
                        .into(imagenResidence)
                }

            }
        }
    }

    companion object {
        private val residencesDiffCallback = object : DiffUtil.ItemCallback<Residence>() {

            override fun areItemsTheSame(oldItem: Residence, newItem: Residence): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Residence, newItem: Residence): Boolean {
                return oldItem == newItem
            }
        }
    }
}