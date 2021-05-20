package com.residencias.es.ui.profile

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.residencias.es.R
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.data.residence.*
import com.residencias.es.databinding.FragmentMyResidenceBinding
import com.residencias.es.utils.Status
import com.residencias.es.viewmodel.MyResidenceViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class MyResidenceFragment : Fragment() {

    private val myResidenceViewModel: MyResidenceViewModel by viewModel()

    private var _binding: FragmentMyResidenceBinding? = null
    private val binding get() = _binding!!

    private var idProvince: Int = 0
    private var idTown: Int = 0

    private val listDependence = mutableMapOf<Int, Int>()
    private var listSectors = mutableMapOf<Int, Int>()
    private var listRooms = mutableMapOf<Int, Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyResidenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        getMyResidence()

        initObservers()

        binding.btnSave.setOnClickListener {
            updateMyResidence()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    private fun getMyResidence() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                myResidenceViewModel.getMyResidence()
                myResidenceViewModel.getAllRooms()
                myResidenceViewModel.getAllSectors()
                myResidenceViewModel.getAllDependences()
            } catch (t: UnauthorizedException) {
                onUnauthorized()
            }
        }
    }

    private fun updateMyResidence() {
        val residence: Residence = getResidenceInfo()

        var dependence = ""
        var sector = ""
        var room = ""

        var checkBox: CheckBox

        for((position, value) in listDependence){
            checkBox = binding.root.findViewById<CheckBox>(position)
            if (checkBox.isChecked ) {
                dependence = dependence.plus("${value}_")
            }
        }

        for((position, value) in listSectors){
            checkBox = binding.root.findViewById<CheckBox>(position)
            if (checkBox.isChecked ) {
                sector = sector.plus("${value}_")
            }
        }

        for((position, value) in listRooms){
            checkBox = binding.root.findViewById<CheckBox>(position)
            if (checkBox.isChecked ) {
                room = room.plus("${value}_")
            }
        }

        try {
            myResidenceViewModel.updateMyResidence(residence, dependence, sector, room)
            Toast.makeText(context, resources.getString(R.string.toast_update_my_residence), Toast.LENGTH_SHORT ).show()
        } catch (t: UnauthorizedException) {
            Toast.makeText(context,resources.getString(R.string.toast_update_my_residence_error), Toast.LENGTH_SHORT ).show()
            onUnauthorized()
        }
    }

    private fun setResidenceInfo(residence: Residence) {
        idProvince = residence.idprovince?.toInt()?:0
        idTown = residence.idtown?.toInt()?:0

        binding.residenceName.setText(residence.name ?: "")
        binding.email.setText(residence.email ?: "")
        binding.web.setText(residence.web ?: "")
        binding.price.setText(residence.price ?: "")
        binding.phone.setText(residence.phone ?: "1")
        binding.address.setText(residence.address ?: "")
        binding.description.setText(residence.description ?: "")
        binding.latitude.setText(residence.latitude ?: "")
        binding.longitude.setText(residence.longitude ?: "")
    }

    private fun getResidenceInfo(): Residence {
        return Residence(
                "",
                "${binding.residenceName.text}",
                "${binding.email.text}",
                "${binding.web.text}",
                "${binding.price.text}",
                "",
                "${binding.phone.text}",
                "${binding.address.text}",
                "${binding.province}",
                "${binding.town}",
                "",
                "${binding.description.text}",
                "${binding.latitude.text}",
                "${binding.longitude.text}",
                "$idProvince",
                "$idTown",
        )
    }

    private fun initObservers() {
        myResidenceViewModel.user.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { residence ->
                        setResidenceInfo(residence)
                    }
                    binding.progressBar.visibility = View.GONE
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    //showError(getString(R.string.error_profile))
                }
            }
        })

        observeProvince()
        observeTown()
        observeRooms()
        observeDependences()
        observeSectors()
    }

    private fun observeProvince() {
        myResidenceViewModel.provinces.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {

                    val provinces = it.data.orEmpty() as ArrayList<Province>
                    provinces.add(0, Province(0, context?.resources?.getString(R.string.spinner_province)))

                    val adapter = activity?.applicationContext?.let { it1 -> ProvincesAdapterMyResidence(it1, idProvince, provinces) }
                    binding.province.adapter = adapter

                    if(idProvince > 0) {
                        val spinnerPosition = adapter?.getPosition()
                        if (spinnerPosition != null) {
                            binding.province.setSelection(spinnerPosition)
                        }
                        myResidenceViewModel.getTowns(idProvince)
                    }

                    binding.province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if (provinces[position].id > 0) {
                                idProvince = provinces[position].id
                                myResidenceViewModel.getTowns(provinces[position].id)
                            } else {
                                idProvince = 0
                                idTown = 0
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
        myResidenceViewModel.towns.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {

                    val towns = it.data.orEmpty() as ArrayList<Town>
                    towns.add(0, Town(0, context?.resources?.getString(R.string.spinner_town)))

                    val adapter = activity?.applicationContext?.let { it1 -> TownsAdapterMyResidence(it1, idTown, towns) }
                    binding.town.adapter = adapter

                    if(idTown > 0) {
                        val spinnerPosition = adapter?.getPosition()
                        if (spinnerPosition != null) {
                            binding.town.setSelection(spinnerPosition)
                        }
                    }

                    binding.town.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            idTown = if (towns[position].id > 0) {
                                towns[position].id
                            } else {
                                0
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
        myResidenceViewModel.rooms.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {

                    val rooms = it.data.orEmpty() as ArrayList<Room>

                    for (position in rooms.indices) {

                        val tv1 = CheckBox(context)
                        val params1: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, // This will define text view width
                                LinearLayout.LayoutParams.WRAP_CONTENT // This will define text view height
                        )

                        params1.setMargins(10, 10, 10, 10)

                        tv1.id = View.generateViewId()
                        tv1.layoutParams = params1
                        tv1.text = rooms[position].room
                        tv1.setTextColor(resources.getColor(R.color.colorPrimary, null))
                        tv1.textSize = 14.0F
                        tv1.setTypeface(tv1.typeface, Typeface.BOLD)

                        tv1.isChecked = false
                        rooms[position].idResidence?.let {
                            tv1.isChecked = true
                        }

                        binding.roomsList.addView(tv1)

                        listRooms[tv1.id] = rooms[position].id
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

    private fun observeSectors() {
        myResidenceViewModel.sectors.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {

                    val sectors = it.data.orEmpty() as ArrayList<Sector>

                    for (position in sectors.indices) {
                        val tv1 = CheckBox(context)
                        val params1: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, // This will define text view width
                                LinearLayout.LayoutParams.WRAP_CONTENT // This will define text view height
                        )

                        params1.setMargins(10, 10, 10, 10)

                        tv1.id = View.generateViewId()
                        tv1.layoutParams = params1
                        tv1.text = sectors[position].sector
                        tv1.setTextColor(resources.getColor(R.color.colorPrimary, null))
                        tv1.textSize = 14.0F
                        tv1.setTypeface(tv1.typeface, Typeface.BOLD)

                        tv1.isChecked = false
                        sectors[position].idResidence?.let {
                            tv1.isChecked = true
                        }

                        binding.sectorsList.addView(tv1)

                        listSectors[tv1.id] = sectors[position].id
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

    private fun observeDependences() {
        myResidenceViewModel.dependences.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {

                    val dependences = it.data.orEmpty() as ArrayList<Dependence>

                    for (position in dependences.indices) {

                        val tv1 = CheckBox(context)
                        val params1: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, // This will define text view width
                                LinearLayout.LayoutParams.WRAP_CONTENT // This will define text view height
                        )

                        params1.setMargins(10, 10, 10, 10)

                        tv1.id = View.generateViewId()
                        tv1.layoutParams = params1
                        tv1.text = dependences[position].dependence
                        tv1.setTextColor(resources.getColor(R.color.colorPrimary, null))
                        tv1.textSize = 14.0F
                        tv1.setTypeface(tv1.typeface, Typeface.BOLD)

                        tv1.isChecked = false
                        dependences[position].idResidence?.let {
                            tv1.isChecked = true
                        }

                        binding.dependencesList.addView(tv1)

                        listDependence[tv1.id] = dependences[position].id
                    }
                    binding.progressBar.visibility = View.GONE
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    //Toast.makeText(this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun onUnauthorized() {
        myResidenceViewModel.onUnauthorized()
    }
}

class ProvincesAdapterMyResidence(context: Context, private var idProvince: Int, private var provinces: List<Province>) :
        ArrayAdapter<Province>(context, idProvince, provinces) {
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(parent?.context).inflate(R.layout.simple_spinner_item, parent, false) as TextView
        view.text = provinces[position].province
        return view
    }

    fun getPosition(): Int {
        for (position in provinces.indices) {
            if (provinces[position].id == idProvince )
                return position
        }
        return 0
    }
}

class TownsAdapterMyResidence(context: Context, private var idtown: Int, private var towns: List<Town>) :
        ArrayAdapter<Town>(context, idtown, towns as MutableList<Town>) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(R.layout.simple_spinner_item, parent, false) as TextView
        view.text = towns[position].town
        return view
    }

    fun getPosition(): Int {
        for (position in towns.indices) {
            if (towns[position].id == idtown )
                return position
        }
        return 0
    }
}