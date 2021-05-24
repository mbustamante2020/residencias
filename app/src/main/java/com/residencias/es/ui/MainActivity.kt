package com.residencias.es.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.residencias.es.R
import com.residencias.es.data.residence.model.Search
import com.residencias.es.databinding.ActivityMainBinding
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

    private lateinit var search: Search
    //private var isMap: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //campos de busqueda para residencias
        try {
            val intent: Intent = intent
            search = intent.getParcelableExtra("search") ?: Search()
            Log.i("MainActivity", "---> success isMap ${search.is_map}")
        } catch (e: NullPointerException) {
            search = Search()
            search.is_map = 0
            Log.i("MainActivity", "---> error isMap ${search.is_map}")
        }







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

        Log.i("MainActivity", "---> isMap $search.is_map")

        if( search.is_map == 1 ) {
            displayMenu(R.id.nav_map)
        } else {
            displayMenu(R.id.nav_search)
        }


    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private var miActionProgressItem: MenuItem? = null
/*
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.menu.activity_main_right)

        // Return to finish
        return super.onPrepareOptionsMenu(menu)
    }
    */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_right, menu)
        miActionProgressItem = menu.findItem(R.menu.activity_main_right)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when (item.itemId) {
           R.id.action_search -> {
               val intent = Intent(this, ResidencesSearchActivity::class.java)
               intent.putExtra("search", search)
               startActivity(intent)
               true
           }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayMenu(id: Int) {
        when (id) {
            R.id.nav_search -> {
                search.is_map = 0
                miActionProgressItem?.isVisible = true
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, ResidencesFragment(search)).commit()
            }

            R.id.nav_residence -> {
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, MyResidenceFragment()).commit()
            }

            R.id.nav_map -> {
                search.is_map = 1
                miActionProgressItem?.isVisible = false
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
            val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
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

