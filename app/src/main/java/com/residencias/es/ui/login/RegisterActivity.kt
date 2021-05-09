package com.residencias.es.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.residencias.es.R
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.databinding.ActivityRegisterBinding
import com.residencias.es.ui.MainActivity
import com.residencias.es.utils.Status
import com.residencias.es.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeRegister()

        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.btnRegister.setOnClickListener {
            register()
        }
    }


    private fun register() {
        lifecycleScope.launch {
            try {

                if( binding.name.text.isNullOrEmpty() || binding.email.text.isNullOrEmpty() || binding.password.text.isNullOrEmpty() ) {
                    Toast.makeText(this@RegisterActivity, getString(R.string.error_register_empty_fields), Toast.LENGTH_SHORT).show()

                } else if( binding.password.text != binding.passwordConfirmation.text  ) {
                    Toast.makeText(this@RegisterActivity, getString(R.string.error_register_passwords_do_not_match), Toast.LENGTH_SHORT).show()

                } else {
                    registerViewModel.register("${binding.name.text}", "${binding.email.text}", "${binding.password.text}")
                }

            } catch (t: UnauthorizedException) {
                Toast.makeText(this@RegisterActivity, getString(R.string.error_register), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun observeRegister() {
        registerViewModel.getToken.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(this, getString(R.string.error_register), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}