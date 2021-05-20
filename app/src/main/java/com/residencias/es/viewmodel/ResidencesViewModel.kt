package com.residencias.es.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.residencias.es.R
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.data.residence.Residence
import com.residencias.es.data.residence.ResidencesRepository
import com.residencias.es.data.residence.Search
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

    suspend fun getAllResidences(nextPage: Int?, search: Search?) {
        _residences.postValue(Resource.loading(null))

        Log.i("ResidencesViewModel", "32")
        repository.getResidences(nextPage, search).let { residences ->
            Log.i("ResidencesViewModel", "34 ${residences?.first}")
             residences?.second?.let {
                 Log.i("ResidencesViewModel", "36")
                 _residences.postValue(Resource.success(residences))
                 Log.i("ResidencesViewModel", "38")
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