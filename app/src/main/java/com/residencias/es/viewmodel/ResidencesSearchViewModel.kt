package com.residencias.es.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.data.residence.*
import com.residencias.es.utils.Resource


class ResidencesSearchViewModel(
        private val repository: ResidencesRepository,
        private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _search = MutableLiveData<Boolean?>()
    val search: LiveData<Boolean?>
        get() = _search

    fun searchResidences() {
        _search.value = true
    }

    private val _provinces = MutableLiveData<Resource<List<Province>?>>()
    val provinces: LiveData<Resource<List<Province>?>>
        get() = _provinces

    suspend fun getAllProvinces(all: Boolean? = null) {
        _provinces.postValue(Resource.loading(null))

        repository.getProvinces(all).let {
            it?.let {
                _provinces.postValue(Resource.success(it))
            } ?: run {
                _provinces.postValue(Resource.error("Error al intentar cargar las provincias", null))
            }
        }
    }

    private val _towns = MutableLiveData<Resource<List<Town>?>>()
    val towns: LiveData<Resource<List<Town>?>>
        get() = _towns

    suspend fun getAllTowns(idprovince: Int, todas: Boolean? = null) {
        _towns.postValue(Resource.loading(null))

        repository.getTowns(idprovince, todas).let {
            it?.let {
                _towns.postValue(Resource.success(it))
            } ?: run {
                _towns.postValue(Resource.error("Error al intentar cargar los municipios", null))
            }
        }
    }

    private val _rooms = MutableLiveData<Resource<List<Room>?>>()
    val rooms: LiveData<Resource<List<Room>?>>
        get() = _rooms

    suspend fun getAllRooms() {
        _rooms.postValue(Resource.loading(null))

        repository.getRooms().let {
            it?.let {
                _rooms.postValue(Resource.success(it))
            } ?: run {
                _rooms.postValue(Resource.error("Error al intentar cargar las habitaciones", null))
            }
        }
    }

    private val _sectors = MutableLiveData<Resource<List<Sector>?>>()
    val sectors: LiveData<Resource<List<Sector>?>>
        get() = _sectors

    suspend fun getAllSectors() {
        _sectors.postValue(Resource.loading(null))

        repository.getSectors().let {
            it?.let {
                _sectors.postValue(Resource.success(it))
            } ?: run {
                _sectors.postValue(Resource.error("Error al intentar cargar las plazas", null))
            }
        }
    }

    private val _dependences = MutableLiveData<Resource<List<Dependence>?>>()
    val dependences: LiveData<Resource<List<Dependence>?>>
        get() = _dependences

    suspend fun getAllDependences() {
        _dependences.postValue(Resource.loading(null))

        repository.getDependences().let {
            it?.let {
                _dependences.postValue(Resource.success(it))
            } ?: run {
                _dependences.postValue(Resource.error("Error al intentar cargar las dependencias", null))
            }
        }
    }

    private val _prices = MutableLiveData<Resource<List<Price>?>>()
    val prices: LiveData<Resource<List<Price>?>>
        get() = _prices

    suspend fun getAllPrices() {
        _prices.postValue(Resource.loading(null))

        repository.getPrices().let {
            it?.let {
                _prices.postValue(Resource.success(it))
            } ?: run {
                _prices.postValue(Resource.error("Error al intentar cargar los precios", null))
            }
        }
    }

    fun onUnauthorized() {
        authenticationRepository.onUnauthorized()
    }
}