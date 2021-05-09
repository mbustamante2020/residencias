package com.residencias.es.ui.residences

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.residencias.es.R
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.data.residences.*
import com.residencias.es.databinding.ActivityResidencesSearchBinding
import com.residencias.es.ui.MainActivity
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
        getProvinces()
        getRooms()
        getSectors()
        getDependences()

        observeProvince()
        observeTown()
        observeRooms()
        observeDependences()
        observeSectors()

        binding.btnSearch.setOnClickListener{
            residencesSearchViewModel.searchResidences()
        }

        residencesSearchViewModel.search.observe(this, {

            search.search_for = "${binding.searchFor.text}"
          /*
            search.sectors_disponibles = "${binding.searchPor.text}"
            search.menores_65 = "${binding.searchPor.text}"*/

            val intent = Intent(this, MainActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("search", search)
            intent.putExtra("Bundle", bundle)


            startActivity(intent)
            /*
            val intent = Intent().apply {
                setClass(baseContext, MainActivity::class.java)
                putExtra("search", search)
            }
            startActivity(intent)
*/




            startActivity(intent)
        })
    }

    private fun getProvinces() {
        lifecycleScope.launch {
            try {
                residencesSearchViewModel.getAllProvinces()
            } catch (t: UnauthorizedException) {
                Log.i("getProvinces ----> ", "getProvinces $t")
            }
        }
    }

    private fun getTowns(idprovincia: Int) {
        lifecycleScope.launch {
            try {
                residencesSearchViewModel.getAllTowns(idprovincia)
            } catch (t: UnauthorizedException) {
                Log.i("getProvinces ----> ", "getProvinces $t")
            }
        }
    }

    private fun getRooms() {
        lifecycleScope.launch {
            try {
                residencesSearchViewModel.getAllRooms()
            } catch (t: UnauthorizedException) {
                Log.i("getRooms ----> ", "getRooms $t")
            }
        }
    }

    private fun getSectors() {
        lifecycleScope.launch {
            try {
                residencesSearchViewModel.getAllSectors()
            } catch (t: UnauthorizedException) {
                Log.i("getSectors ----> ", "getSectors $t")
            }
        }
    }

    private fun getDependences() {
        lifecycleScope.launch {
            try {
                residencesSearchViewModel.getAllDependences()
            } catch (t: UnauthorizedException) {
                Log.i("getDependences ----> ", "getDependences $t")
            }
        }
    }



    private fun observeProvince() {
        residencesSearchViewModel.provincias.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    val provincias = it.data.orEmpty() as ArrayList<Province>
                    provincias.add(0, Province(0, "Selecciona una provincia"))

                    val adapter = ProvincesAdapter(this, R.layout.simple_spinner_item, provincias)
                    binding.province.adapter = adapter

                    binding.province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if( provincias[position].id > 0 ) {
                                //Toast.makeText(this@ResidencesSearchActivity,"Item seleccionado " + provincias[position], Toast.LENGTH_SHORT ).show()
                                search.province = provincias[position].id
                                getTowns(provincias[position].id)
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
                    Log.i("getProvinces ----> ", "observerSearch ERROR")
                    /* if (adapter?.currentList.isNullOrEmpty()) {
                        Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
                    }*/
                }
            }
        })
    }

    private fun observeTown() {
        residencesSearchViewModel.towns.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {

                    val towns = it.data.orEmpty() as ArrayList<Town>
                    towns.add(0, Town(0, "Selecciona un town"))

                    val adapter = TownsAdapter(this, R.layout.simple_spinner_item, towns)
                    binding.town.adapter = adapter

                    binding.town.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected( parent: AdapterView<*>, view: View, position: Int, id: Long ) {
                            if( towns[position].id > 0 ) {
                                //Toast.makeText(this@ResidencesSearchActivity, "Item seleccionado " + towns[position], Toast.LENGTH_SHORT).show()
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
                    Log.i("getProvinces ----> ", "observerSearch ERROR")
                    /* if (adapter?.currentList.isNullOrEmpty()) {
                        Toast.makeText( this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT ).show()
                    }*/
                }
            }
        })
    }

    private fun observeRooms() {
        residencesSearchViewModel.rooms.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {

                    val rooms = it.data.orEmpty() as ArrayList<Room>
                    rooms.add(0, Room(0, "Selecciona una habitación"))

                    val adapter = RoomsAdapter(this, R.layout.simple_spinner_item, rooms)
                    binding.room.adapter = adapter

                    binding.room.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected( parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if( rooms[position].id > 0 ) {
                                //Toast.makeText(this@ResidencesSearchActivity, "Item seleccionado " + rooms[position], Toast.LENGTH_SHORT).show()
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
                    Log.i("getProvinces ----> ", "observerSearch ERROR")
                    /* if (adapter?.currentList.isNullOrEmpty()) {
                        Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
                    }*/
                }
            }
        })
    }

    private fun observeSectors() {
        residencesSearchViewModel.sectors.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {

                    val sectors = it.data.orEmpty() as ArrayList<Sector>
                    sectors.add(0, Sector(0, "Selecciona una sector"))

                    val adapter = SectorsAdapter(this, R.layout.simple_spinner_item, sectors)
                    binding.sectors.adapter = adapter

                    binding.sectors.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected( parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if( sectors[position].id > 0 ) {
                                //Toast.makeText(this@ResidencesSearchActivity, "Item seleccionado " + sectors[position], Toast.LENGTH_SHORT).show()
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
                    Log.i("getProvinces ----> ", "observerSearch ERROR")
                    /* if (adapter?.currentList.isNullOrEmpty()) {
                        Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
                    }*/
                }
            }
        })
    }

    private fun observeDependences() {
        residencesSearchViewModel.dependences.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {

                    val dependences = it.data.orEmpty() as ArrayList<Dependence>
                    dependences.add(0, Dependence(0, "Selecciona una dependence"))

                    val adapter = DependencesAdapter(this, R.layout.simple_spinner_item, dependences)
                    binding.dependence.adapter = adapter

                    binding.dependence.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected( parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if( dependences[position].id > 0 ) {
                                Toast.makeText(this@ResidencesSearchActivity, "Item seleccionado " + dependences[position], Toast.LENGTH_SHORT).show()
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
                    Log.i("getProvinces ----> ", "observerSearch ERROR")
                    /* if (adapter?.currentList.isNullOrEmpty()) {
                        Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
                    }*/
                }
            }
        })
    }
}











class ProvincesAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val provincias: List<Province>):
        ArrayAdapter<Province>(context, layoutResource, provincias) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = provincias[position].province
        return view
    }
}

class TownsAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val towns: List<Town>):
        ArrayAdapter<Town>(context, layoutResource, towns) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = towns[position].town
        return view
    }
}

class RoomsAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val rooms: List<Room>):
        ArrayAdapter<Room>(context, layoutResource, rooms) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = rooms[position].room
        return view
    }
}

class SectorsAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val sectors: List<Sector>):
        ArrayAdapter<Sector>(context, layoutResource, sectors) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = sectors[position].sector
        return view
    }
}

class DependencesAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val dependences: List<Dependence>):
        ArrayAdapter<Dependence>(context, layoutResource, dependences) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = dependences[position].dependence
        return view
    }
}