package com.residencias.es.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.residencias.es.R
import com.residencias.es.data.residence.Search
import com.residencias.es.databinding.ActivityMainBinding
import com.residencias.es.databinding.SwitchItemBinding
import com.residencias.es.ui.login.LoginActivity
import com.residencias.es.ui.map.ResidencesMapsFragment
import com.residencias.es.ui.photo.PhotosFragment
import com.residencias.es.ui.profile.MyResidenceFragment
import com.residencias.es.ui.profile.ProfileFragment
import com.residencias.es.ui.residence.ResidencesFragment
import com.residencias.es.ui.residence.ResidencesSearchActivity
import com.residencias.es.viewmodel.MainViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    private var search: Search? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //campos de busqueda para la busqueda de residencias
        val intent: Intent = intent
        search = intent.getParcelableExtra("search")

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        //Si el usuario no es una residencia, se ocultan "Mi residencia" y "Mis fotografías"
        if (!mainViewModel.isResidence()) {
            binding.navView.menu.findItem(R.id.nav_residence).isVisible = false
            binding.navView.menu.findItem(R.id.nav_photo).isVisible = false

        }

        displayMenu(R.id.nav_search)
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
        Log.i("switch", "${item.itemId}")
       return when (item.itemId) {

           R.id.action_search -> {
               startActivity(Intent(this, ResidencesSearchActivity::class.java))
               true
           }
           R.id.app_bar_switch -> {
               val toolbar = findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.near_switch)
               Log.i("switch", "${toolbar.isChecked}")
               true
           }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayMenu(id: Int) {

        when (id) {

            R.id.nav_search -> {
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, ResidencesFragment(search)).commit()
            }

            R.id.nav_residence -> {
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, MyResidenceFragment()).commit()
            }

            R.id.nav_map -> {
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, ResidencesMapsFragment(search)).commit()
            }

            R.id.nav_photo -> {
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, PhotosFragment()).commit()
            }

            R.id.nav_profile -> {
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, ProfileFragment()).commit()
            }

            R.id.nav_close -> {
                logout()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        displayMenu(item.itemId)
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        mainViewModel.logout()

        try {
            //Cerrar sesion de google
            FirebaseApp.initializeApp(this)

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            var mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        } catch (e: Exception) {

        }

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

