package com.residencias.es.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.residencias.es.R
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.data.user.User
import com.residencias.es.databinding.FragmentProfileBinding
import com.residencias.es.ui.MainActivity
import com.residencias.es.ui.login.LoginActivity
import com.residencias.es.utils.Status
import com.residencias.es.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModel()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        getUserProfile()
        observerUser()

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                updateUserDescription()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////
    private fun getUserProfile() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                profileViewModel.getUser()

                profileViewModel.user.observe(viewLifecycleOwner, Observer {
                    when (it.status) {
                        Status.SUCCESS -> {

                            it.data?.let { user ->
                                setUserInfo(user)
                            }
                            binding.progressBar.visibility = View.GONE
                        }
                        Status.LOADING -> {

                        }
                        Status.ERROR -> {

                        }
                    }
                })
            } catch (t: UnauthorizedException) {
                onUnauthorized()
            }
        }
    }

    private fun updateUserDescription() {
        val user = User(null,
                "${binding.userName.text}",
                "${binding.name.text}",
                "${binding.email.text}",
                "${binding.phone.text}",
                "${binding.address.text}"
        )

        try {
            profileViewModel.updateUser(user)
            Toast.makeText(context, resources.getString(R.string.toast_update_profile), Toast.LENGTH_SHORT ).show()
        } catch (t: UnauthorizedException) {
            Toast.makeText(context,resources.getString(R.string.toast_update_profile_error), Toast.LENGTH_SHORT ).show()
            onUnauthorized()
        }
    }

    private fun setUserInfo(user: User) {
        binding.userName.setText(user.userName ?: "")
        binding.name.setText(user.name ?: "")
        binding.address.setText(user.address ?: "")
        binding.phone.setText(user.phone ?: "")
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

    private fun observerUser() {
        profileViewModel.user.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { user ->
                        setUserInfo(user)
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
            }
        })
    }

    private fun onUnauthorized() {
        val intent = Intent(activity, LoginActivity::class.java)
        (activity as MainActivity?)!!.startActivity(intent)
    }
}