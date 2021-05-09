package com.residencias.es.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.data.user.User
import com.residencias.es.databinding.FragmentProfileBinding
import com.residencias.es.utils.Status
import com.residencias.es.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModel()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        // se obtienen los datos del perfil de usuario
        lifecycleScope.launch {
            getUserProfile()
        }


        // Update Description Button Listener
        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                val user = User (null,
                    "${binding.userName.text}",
                    "${binding.name.text}",
                    "${binding.email.text}",
                    "${binding.phone.text}",
                    "${binding.address.text}"
                )
                updateUserDescription(user)
            }
        }

        profileViewModel.user.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                            user -> setUserInfo(user)
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    // Error :(
                    //showError(getString(R.string.error_profile))
                }
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////
    private fun getUserProfile() {
        binding.progressBar.visibility = View.VISIBLE
        Log.i("profile", "----> 89")
        try {
            profileViewModel.getUser()

             profileViewModel.user.observe(viewLifecycleOwner, Observer {
                 when (it.status) {
                     Status.SUCCESS -> {
                         Log.i("profile", "----> SUCCESS ${it.data}")

                         it.data?.let {
                             user -> setUserInfo(user)
                         }
                         binding.progressBar.visibility = View.GONE
                     }
                     Status.LOADING -> {
                         Log.i("profile", "----> LOADING")
                     }
                     Status.ERROR -> {
                         // Error :(
                         Log.i("profile", "----> ERROR")
                         //showError(getString(R.string.error_profile))
                     }
                 }
             })
            // Hide Loading
           // binding.progressBar.visibility = View.GONE
        } catch (t: UnauthorizedException) {
            Log.i("profile", "----> ERROR 115")
            onUnauthorized()
        }
    }

    private fun updateUserDescription(user: User?) {
        //binding.progressBar.visibility = View.VISIBLE
        try {
            profileViewModel.updateUser(user)

            /*profileViewModel.user.observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let {
                            user -> setUserInfo(user)
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        // Error :(
                        showError(getString(R.string.error_profile))
                    }
                }
            })*/
            // Hide Loading
            binding.progressBar.visibility = View.GONE
        } catch (t: UnauthorizedException) {
            onUnauthorized()
        }
    }

    private fun setUserInfo(user: User) {
        // Set Texts
        Log.i("profile", "----> ${user.email}")
        binding.userName.setText(user.userName ?: "")
        binding.name.setText(user.name ?: "")
        binding.address.setText(user.address ?: "")
        binding.phone.setText(user.phone ?: "1")
        binding.email.setText(user.email ?: "")


     /*   // Avatar Image
        user.profileImageUrl?.let {
            Glide.with(this)
                .load(user.getSizedImage(it, 128, 128))
                .centerCrop()
                .transform(CircleCrop())
                .into(binding.imagenResidence)
        }*/
    }


    private fun onUnauthorized() {
        // Clear local access token
        profileViewModel.onUnauthorized()
        // User was logged out, close screen and all parent screens and open login
        //finishAffinity()
        //startActivity(Intent(this, LoginActivity::class.java))
    }


}