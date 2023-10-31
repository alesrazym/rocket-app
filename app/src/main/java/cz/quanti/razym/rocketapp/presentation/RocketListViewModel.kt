package cz.quanti.razym.rocketapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.razym.rocketapp.domain.RocketModel
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import kotlinx.coroutines.launch

class RocketListViewModel(
    private val repo: RocketsRepository
)  : ViewModel() {
    private val _rocketLiveData: MutableLiveData<Resource<List<RocketModel>>> = MutableLiveData()
    val rocketLiveData: LiveData<Resource<List<RocketModel>>> = _rocketLiveData

    init {
        refreshRockets()
    }

    fun refreshRockets() {
        viewModelScope.launch {
            _rocketLiveData.postValue(Resource.loading(null))
            _rocketLiveData.postValue(repo.getRockets())
        }
    }
}
