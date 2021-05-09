package com.residencias.es.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.residencias.es.databinding.ActivitySplashBinding
import com.residencias.es.ui.login.LoginActivity
import com.residencias.es.viewmodel.SplashViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModel()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashViewModel.getUserAvailability()

        Handler().postDelayed({
           checkUserSession()
        },500)
    }

    private fun checkUserSession() {
        splashViewModel.isUserAvailable.observe( this, { isUserAvailable ->
            if (isUserAvailable) {
                // el usuario ya inicio un sesión previamente
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // el usuario desde iniciar sesión
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        })
    }
}