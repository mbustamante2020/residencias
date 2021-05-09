package com.residencias.es.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.data.residences.*
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

    private val _provincias = MutableLiveData<Resource<List<Province>?>>()
    val provincias: LiveData<Resource<List<Province>?>>
        get() = _provincias

    suspend fun getAllProvinces(todas: Boolean? = null) {
        _provincias.postValue(Resource.loading(null))

        repository.getProvinces(todas).let { provincias ->
            provincias?.let {
                _provincias.postValue(Resource.success(provincias))
            } ?: run {
                _provincias.postValue(Resource.error("Error al intentar cargar las provincias", null))
            }
        }
    }

    private val _towns = MutableLiveData<Resource<List<Town>?>>()
    val towns: LiveData<Resource<List<Town>?>>
        get() = _towns

    suspend fun getAllTowns(idprovincia: Int, todas: Boolean? = null) {
        _towns.postValue(Resource.loading(null))

        repository.getTowns(idprovincia, todas).let { towns ->
            towns?.let {
                _towns.postValue(Resource.success(towns))
            } ?: run {
                _towns.postValue(Resource.error("Error al intentar cargar los towns", null))
            }
        }
    }

    private val _rooms = MutableLiveData<Resource<List<Room>?>>()
    val rooms: LiveData<Resource<List<Room>?>>
        get() = _rooms

    suspend fun getAllRooms() {
        _rooms.postValue(Resource.loading(null))

        repository.getRooms().let { rooms ->
            rooms?.let {
                _rooms.postValue(Resource.success(rooms))
            } ?: run {
                _rooms.postValue(Resource.error("Error al intentar cargar las rooms", null))
            }
        }
    }

    private val _sectors = MutableLiveData<Resource<List<Sector>?>>()
    val sectors: LiveData<Resource<List<Sector>?>>
        get() = _sectors

    suspend fun getAllSectors() {
        _sectors.postValue(Resource.loading(null))

        repository.getSectors().let { sectors ->
            sectors?.let {
                _sectors.postValue(Resource.success(sectors))
            } ?: run {
                _sectors.postValue(Resource.error("Error al intentar cargar las sectors", null))
            }
        }
    }

    private val _dependences = MutableLiveData<Resource<List<Dependence>?>>()
    val dependences: LiveData<Resource<List<Dependence>?>>
        get() = _dependences

    suspend fun getAllDependences() {
        _dependences.postValue(Resource.loading(null))

        repository.getDependences().let { dependences ->
            dependences?.let {
                _dependences.postValue(Resource.success(dependences))
            } ?: run {
                _dependences.postValue(Resource.error("Error al intentar cargar las dependences", null))
            }
        }
    }

    fun onUnauthorized() {
        authenticationRepository.onUnauthorized()
    }
}