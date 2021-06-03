package com.residencias.es.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.residencias.es.R
import com.residencias.es.data.residence.ResidencesRepository
import com.residencias.es.data.residence.model.*
import com.residencias.es.utils.Resource
import kotlinx.coroutines.launch


class MyResidenceViewModel(
    private val residencesRepository: ResidencesRepository
    //private val oAuthRepository: OAuthRepository
) : ViewModel() {

    private val _user = MutableLiveData<Resource<Residence?>>()
    val user: LiveData<Resource<Residence?>>
        get() = _user

    private val _provinces = MutableLiveData<Resource<List<Province>?>>()
    val provinces: LiveData<Resource<List<Province>?>>
        get() = _provinces

    private val _towns = MutableLiveData<Resource<List<Town>?>>()
    val towns: LiveData<Resource<List<Town>?>>
        get() = _towns

    fun getMyResidence() {
        viewModelScope.launch {
            getResidence()
            getProvinces()

        }
    }

    private suspend fun getResidence() {
        _user.postValue(Resource.loading(null))

        residencesRepository.getMyResidence()?.let { response ->
            _user.postValue(Resource.success(response))
        } ?: run {
            _user.postValue(Resource.error(R.string.error_profile.toString(), null))
        }
    }

    private suspend fun getProvinces(all: Boolean? = null) {
        _provinces.postValue(Resource.loading(null))

        residencesRepository.getProvinces(all).let { response ->
            response?.let {
                _provinces.postValue(Resource.success(response))
            } ?: run {
                _provinces.postValue(Resource.error("Error al intentar cargar las provinces", null))
            }
        }
    }

    fun getTowns(idProvince: Int, all: Boolean? = null) {
        viewModelScope.launch {
            _towns.postValue(Resource.loading(null))

            residencesRepository.getTowns(idProvince, all).let { response ->
                response?.let {
                    _towns.postValue(Resource.success(response))
                } ?: run {
                    _towns.postValue(Resource.error("Error al intentar cargar los towns", null))
                }
            }
        }
    }

    fun updateMyResidence(residence: Residence?, dependence: String, sector: String, room: String) {
        viewModelScope.launch {
            _user.postValue(Resource.loading(null))

            residencesRepository.updateMyResidence(residence, dependence, sector, room)?.let { response ->
                // Success :)
                _user.postValue(Resource.success(response))
            } ?: run {
                // Failure :(
                _user.postValue(Resource.error(R.string.error_profile.toString(), null))
            }
        }
    }


    private val _rooms = MutableLiveData<Resource<List<Room>?>>()
    val rooms: LiveData<Resource<List<Room>?>>
        get() = _rooms

    fun getAllRooms() {
        viewModelScope.launch {
            _rooms.postValue(Resource.loading(null))

            residencesRepository.getMyResidenceRooms().let { rooms ->
                rooms?.let {
                    _rooms.postValue(Resource.success(rooms))
                } ?: run {
                    _rooms.postValue(Resource.error("Error al intentar cargar las rooms", null))
                }
            }
        }
    }

    private val _sectors = MutableLiveData<Resource<List<Sector>?>>()
    val sectors: LiveData<Resource<List<Sector>?>>
        get() = _sectors

    fun getAllSectors() {
        viewModelScope.launch {
            _sectors.postValue(Resource.loading(null))

            residencesRepository.getMyResidenceSectors().let { sectors ->
                sectors?.let {
                    _sectors.postValue(Resource.success(sectors))
                } ?: run {
                    _sectors.postValue(Resource.error("Error al intentar cargar las sectors", null))
                }
            }
        }
    }

    private val _dependencies = MutableLiveData<Resource<List<Dependence>?>>()
    val dependencies: LiveData<Resource<List<Dependence>?>>
        get() = _dependencies

    fun getAllDependencies() {
        viewModelScope.launch {
            _dependencies.postValue(Resource.loading(null))

            residencesRepository.getMyResidenceDependencies().let { dependencies ->
                dependencies?.let {
                    _dependencies.postValue(Resource.success(dependencies))
                } ?: run {
                    _dependencies.postValue(Resource.error("Error al intentar cargar las dependencies", null))
                }
            }
        }
    }


}