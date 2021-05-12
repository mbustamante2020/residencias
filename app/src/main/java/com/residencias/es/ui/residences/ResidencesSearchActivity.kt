package com.residencias.es.ui.residences

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.residencias.es.R
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.data.residences.*
import com.residencias.es.databinding.ActivityResidencesSearchBinding
import com.residencias.es.ui.MainActivity
import com.residencias.es.ui.residences.adapter.*
import com.residencias.es.utils.Status
import com.residencias.es.viewmodel.ResidencesSearchViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class ResidencesSearchActivity : AppCompatActivity() {

    private val residencesSearchViewModel: ResidencesSearchViewModel by viewModel()
    private lateinit var binding: ActivityResidencesSearchBinding

    private var search = Search()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResidencesSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE

        getSearchFields()

        initObservers()

        //click en el botón buscar
        binding.btnSearch.setOnClickListener{
            residencesSearchViewModel.searchResidences()
        }
    }

    private fun getSearchFields() {
        lifecycleScope.launch {
            try {
                residencesSearchViewModel.getAllProvinces()
                residencesSearchViewModel.getAllRooms()
                residencesSearchViewModel.getAllSectors()
                residencesSearchViewModel.getAllDependences()
                residencesSearchViewModel.getAllPrices()
            } catch (t: UnauthorizedException) {
                //Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getTowns(idprovince: Int) {
        lifecycleScope.launch {
            try {
                residencesSearchViewModel.getAllTowns(idprovince)
            } catch (t: UnauthorizedException) {
                //Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initObservers() {
        residencesSearchViewModel.search.observe(this, {
            search.search_for = "${binding.searchFor.text}"

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("search", search)
            startActivity(intent)
        })

        observeProvince()
        observeTown()
        observeRooms()
        observeDependences()
        observeSectors()
        observePrices()
    }

    private fun observeProvince() {
        residencesSearchViewModel.provinces.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    val provinces = it.data.orEmpty() as ArrayList<Province>
                    provinces.add(0, Province(0, resources.getString(R.string.spinner_province)))

                    val adapter = SpinnerProvinceAdapter(this, R.layout.simple_spinner_item, provinces)
                    binding.province.adapter = adapter

                    binding.province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if( provinces[position].id > 0 ) {
                                search.province = provinces[position].id
                                getTowns(provinces[position].id)
                            } else {
                                search.province = null
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Code to perform some action when nothing is selected
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    //Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeTown() {
        residencesSearchViewModel.towns.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {

                    val towns = it.data.orEmpty() as ArrayList<Town>
                    towns.add(0, Town(0, resources.getString(R.string.spinner_town)))

                    val adapter = SpinnerTownAdapter(this, R.layout.simple_spinner_item, towns)
                    binding.town.adapter = adapter

                    binding.town.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected( parent: AdapterView<*>, view: View, position: Int, id: Long ) {
                            if( towns[position].id > 0 ) {
                                search.town = towns[position].id
                            } else {
                                search.town = null
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Code to perform some action when nothing is selected
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    //Toast.makeText( this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT ).show()
                }
            }
        })
    }

    private fun observeRooms() {
        residencesSearchViewModel.rooms.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {

                    val rooms = it.data.orEmpty() as ArrayList<Room>
                    rooms.add(0, Room(0, resources.getString(R.string.spinner_room)))

                    val adapter = SpinnerRoomAdapter(this, R.layout.simple_spinner_item, rooms)
                    binding.room.adapter = adapter

                    binding.room.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected( parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if( rooms[position].id > 0 ) {
                                search.room = rooms[position].id
                            } else {
                                search.room = null
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Code to perform some action when nothing is selected
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeSectors() {
        residencesSearchViewModel.sectors.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {

                    val sectors = it.data.orEmpty() as ArrayList<Sector>
                    sectors.add(0, Sector(0, resources.getString(R.string.spinner_sector)))

                    val adapter = SpinnerSectorAdapter(this, R.layout.simple_spinner_item, sectors)
                    binding.sectors.adapter = adapter

                    binding.sectors.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected( parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if( sectors[position].id > 0 ) {
                                search.sector = sectors[position].id
                            } else {
                                search.sector = null
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Code to perform some action when nothing is selected
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeDependences() {
        residencesSearchViewModel.dependences.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {

                    val dependences = it.data.orEmpty() as ArrayList<Dependence>
                    dependences.add(0, Dependence(0, resources.getString(R.string.spinner_dependence)))

                    val adapter = SpinnerDependenceAdapter(this, R.layout.simple_spinner_item, dependences)
                    binding.dependence.adapter = adapter

                    binding.dependence.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected( parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if( dependences[position].id > 0 ) {
                                search.dependence = dependences[position].id
                            } else {
                                search.dependence = null
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Code to perform some action when nothing is selected
                        }
                    }
                    binding.progressBar.visibility = View.GONE
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observePrices() {
        residencesSearchViewModel.prices.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {

                    val prices = it.data.orEmpty() as ArrayList<Price>
                    prices.add(0, Price(0, resources.getString(R.string.spinner_price)))

                    val adapter = SpinnerPriceAdapter(this, R.layout.simple_spinner_item, prices)
                    binding.price.adapter = adapter

                    binding.price.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected( parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if( prices[position].id > 0 ) {
                                search.price = prices[position].id
                            } else {
                                search.price = null
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Code to perform some action when nothing is selected
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}