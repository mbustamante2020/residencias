package com.residencias.es.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.residencias.es.R
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.data.residences.*
import com.residencias.es.utils.Resource
import kotlinx.coroutines.launch


class MyResidenceViewModel(
    private val repository: ResidencesRepository,
    private val authenticationRepository: AuthenticationRepository
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

        repository.getMyResidence(authenticationRepository.getAccessToken())?.let { response ->
            _user.postValue(Resource.success(response))
        } ?: run {
            _user.postValue(Resource.error(R.string.error_profile.toString(), null))
        }
    }

    private suspend fun getProvinces(all: Boolean? = null) {
        _provinces.postValue(Resource.loading(null))

        repository.getProvinces(all).let { response ->
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

            repository.getTowns(idProvince, all).let { response ->
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

            repository.updateMyResidence(authenticationRepository.getAccessToken(), residence, dependence, sector, room)?.let { response ->
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

            repository.getMyResidenceRooms(authenticationRepository.getAccessToken()).let { rooms ->
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

            repository.getMyResidenceSectors(authenticationRepository.getAccessToken()).let { sectors ->
                sectors?.let {
                    _sectors.postValue(Resource.success(sectors))
                } ?: run {
                    _sectors.postValue(Resource.error("Error al intentar cargar las sectors", null))
                }
            }
        }
    }

    private val _dependences = MutableLiveData<Resource<List<Dependence>?>>()
    val dependences: LiveData<Resource<List<Dependence>?>>
        get() = _dependences

    fun getAllDependences() {
        viewModelScope.launch {
            _dependences.postValue(Resource.loading(null))

            repository.getMyResidenceDependences(authenticationRepository.getAccessToken()).let { dependences ->
                dependences?.let {
                    _dependences.postValue(Resource.success(dependences))
                } ?: run {
                    _dependences.postValue(Resource.error("Error al intentar cargar las dependences", null))
                }
            }
        }
    }

    fun onUnauthorized() {
        authenticationRepository.onUnauthorized()
    }
}