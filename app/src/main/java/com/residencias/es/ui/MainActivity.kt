package com.residencias.es.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.residencias.es.R
import com.residencias.es.databinding.ActivityMainBinding
import com.residencias.es.ui.advertising.AdvertisingFragment
import com.residencias.es.ui.login.LoginActivity
import com.residencias.es.ui.message.MessagesFragment
import com.residencias.es.ui.photo.CameraActivity
import com.residencias.es.ui.profile.MyResidenceFragment
import com.residencias.es.ui.profile.ProfileFragment
import com.residencias.es.ui.residences.ResidenceMapsActivity
import com.residencias.es.ui.residences.ResidencesFragment
import com.residencias.es.ui.residences.ResidencesSearchActivity
import com.residencias.es.viewmodel.MainViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, binding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        displayScreen(R.id.nav_search)
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_right, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, ResidencesSearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayScreen(id: Int) {
        when (id) {
            
            R.id.nav_search -> {
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, ResidencesFragment()).commit()
            }

            R.id.nav_residence -> {
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, MyResidenceFragment()).commit()
            }

            R.id.nav_mapa -> {
                startActivity(Intent(this, ResidenceMapsActivity::class.java))
            }

            R.id.nav_mensaje -> {
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, MessagesFragment()).commit()
            }

            R.id.nav_foto -> {
                startActivity(Intent(this, CameraActivity::class.java))
                //supportFragmentManager.beginTransaction().replace(R.id.relativelayout, PhotoFragment()).commit()
            }

            R.id.nav_publicidad -> {
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, AdvertisingFragment()).commit()
            }

            R.id.nav_perfil -> {
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, ProfileFragment()).commit()
            }

            R.id.nav_cerrar -> {
                logout()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        displayScreen(item.itemId)
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        mainViewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}