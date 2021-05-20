package com.residencias.es.ui.photo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.residencias.es.R
import com.residencias.es.data.network.Endpoints
import com.residencias.es.data.photo.Photo
import com.residencias.es.databinding.ItemPhotoBinding
import com.residencias.es.viewmodel.PhotoViewModel


class PhotosAdapter(private val viewModel: PhotoViewModel) :
        ListAdapter<Photo, PhotosAdapter.PhotoViewHolder>(photosDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPhotoBinding.inflate(inflater)

        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bindTo(getItem(position), viewModel)
    }

    class PhotoViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(photo: Photo, viewModel: PhotoViewModel) {
            with(binding) {

                //evento que permite acceder a la ficha de la photo
                itemPhoto.setOnClickListener {
                    viewModel.photoClicked(photo)
                }

                title.text = photo.title

                //se muestra la imagen principal de la photo
                photo.path.let {
                    Glide.with(itemView)
                        .load( "${Endpoints.urlImagen}/${it}-240x160.webp")
                        .error(R.drawable.ic_no_available)
                        .into(residenceImage)

                } /*?: run {
                    Glide.with(itemView)
                        .load(R.drawable.ic_no_available)
                        .into(residenceImage)
                }*/


            }
        }

    }

    companion object {
        private val photosDiffCallback = object : DiffUtil.ItemCallback<Photo>() {

            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem == newItem
            }
        }
    }
}