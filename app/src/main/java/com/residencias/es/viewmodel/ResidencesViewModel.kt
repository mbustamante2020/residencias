package com.residencias.es.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.residencias.es.R
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.data.residences.Residence
import com.residencias.es.data.residences.ResidencesRepository
import com.residencias.es.data.residences.Search
import com.residencias.es.utils.Resource
import kotlinx.coroutines.launch


class ResidencesViewModel(
        private val repository: ResidencesRepository,
        private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _residences = MutableLiveData<Resource<Pair<Int?, List<Residence>?>>>()
    val residences: LiveData<Resource<Pair<Int?, List<Residence>?>>>
        get() = _residences

    val verResidence = MutableLiveData<Boolean>()
    var residence  = MutableLiveData<Residence>()

    suspend fun getAllResidences(pagina: Int?, search: Search?) {
        _residences.postValue(Resource.loading(null))

        repository.getResidences(pagina, search).let { residences ->

             residences?.second?.let {
                 _residences.postValue(Resource.success(residences))

             } ?: run {
                _residences.postValue(Resource.error(R.string.error_residences.toString(), null))

            }
         }
    }

    fun  residenceClicked(residence1: Residence?) {
        viewModelScope.launch {
            //var res: Residence = Residence(id = "", nombre = "$imageUrl")
            residence.postValue(residence1!!)
            verResidence.postValue(true)
        }
    }

    fun onUnauthorized() {
        authenticationRepository.onUnauthorized()
    }
}