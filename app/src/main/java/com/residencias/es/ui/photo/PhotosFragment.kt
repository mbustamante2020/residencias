package com.residencias.es.ui.photo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.data.photo.Photo
import com.residencias.es.databinding.FragmentPhotosBinding
import com.residencias.es.utils.Status
import com.residencias.es.viewmodel.PhotoViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import org.koin.android.viewmodel.ext.android.viewModel

class PhotosFragment : Fragment() {

    private val photoViewModel: PhotoViewModel by viewModel()
    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: PhotosAdapter? = null

    private var photo: Photo? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        initRecyclerView()

        observeResidences()

        observeVerResidence()

        binding.btnTakePicture.setOnClickListener {
            val intent = Intent(activity, PhotoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(activity)
        adapter = PhotosAdapter(photoViewModel)

        binding.recyclerViewPhotos.layoutManager = layoutManager
        binding.recyclerViewPhotos.adapter = adapter
    }

    private fun getImages() {
        lifecycleScope.launch {
            try {
                photoViewModel.getImages()
            } catch (t: UnauthorizedException) {
                // Clear local access token
                photoViewModel.onUnauthorized()
                // User was logged out, close screen and open login
                //finish()
                //startActivity(Intent(this@ResidencesActivity, LoginActivity::class.java))
            }
        }
    }

    private fun observeResidences() {
        photoViewModel.photos.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    val photos = it.data?.second.orEmpty()
                    adapter?.submitList(photos)
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Log.i("ResidencesFragment", "error !!!! ${it.toString()}")
                }
            }
        })
    }

    private fun observeVerResidence() {
        photoViewModel.editImage.observe(viewLifecycleOwner, { editImage ->
            if (editImage) {
                val intent = Intent(activity, PhotoActivity::class.java)
                photo = photoViewModel.photo.value
                intent.putExtra("photo", photoViewModel.photo.value)
                startActivity(intent)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        getImages()
    }
}