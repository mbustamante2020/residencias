package com.residencias.es.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.residencias.es.R
import com.residencias.es.data.residence.ResidencesRepository
import com.residencias.es.data.residence.model.Residence
import com.residencias.es.data.residence.model.Search
import com.residencias.es.utils.Resource
import kotlinx.coroutines.launch


class ResidencesViewModel(
        private val residencesRepository: ResidencesRepository
) : ViewModel() {

    private val _residences = MutableLiveData<Resource<Pair<Int?, List<Residence>?>>>()
    val residences: LiveData<Resource<Pair<Int?, List<Residence>?>>>
        get() = _residences

    val verResidence = MutableLiveData<Boolean>()
    var residence  = MutableLiveData<Residence>()

    suspend fun getResidences(nextPage: Int?, search: Search?) {
        _residences.postValue(Resource.loading(null))
        residencesRepository.getResidences(nextPage, search).let { residences ->
             residences?.second?.let {
                 _residences.postValue(Resource.success(residences))
             } ?: run {
                _residences.postValue(Resource.error(R.string.error_residences.toString(), null))

            }
         }
    }

    fun getResidencesMap(nextPage: Int?, search: Search?) {
        viewModelScope.launch {
            _residences.postValue(Resource.loading(null))
            residencesRepository.getResidencesMap(nextPage, search).let { residences ->
                residences?.second?.let {
                    _residences.postValue(Resource.success(residences))
                } ?: run {
                    _residences.postValue(
                        Resource.error(
                            R.string.error_residences.toString(),
                            null
                        )
                    )

                }
            }
        }
    }

    fun  residenceClicked(residence1: Residence?) {
        viewModelScope.launch {
            residence.postValue(residence1!!)
            verResidence.postValue(true)
        }
    }
}