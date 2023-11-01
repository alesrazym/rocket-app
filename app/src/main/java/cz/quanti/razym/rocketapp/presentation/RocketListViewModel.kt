package cz.quanti.razym.rocketapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.model.Rocket
import kotlinx.coroutines.launch

class RocketListViewModel(
    private val repo: RocketsRepository
)  : ViewModel() {
    private val _rocketLiveData: MutableLiveData<Resource<List<Rocket>>> = MutableLiveData()
    val rocketLiveData: LiveData<Resource<List<Rocket>>> = _rocketLiveData

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
