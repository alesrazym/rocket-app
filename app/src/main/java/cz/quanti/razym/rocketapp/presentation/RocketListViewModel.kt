package cz.quanti.razym.rocketapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import kotlinx.coroutines.launch
import cz.quanti.razym.rocketapp.model.Rocket

class RocketListViewModel(
    private val repo: RocketsRepository
)  : ViewModel() {
    private val _rocketLiveData: MutableLiveData<Resource<List<Rocket>>> = MutableLiveData()
    val rocketLiveData: LiveData<Resource<List<Rocket>>> = _rocketLiveData

    init {
        loadRockets()
    }

    fun loadRockets() {
        viewModelScope.launch {
            _rocketLiveData.postValue(Resource.loading(null))
            _rocketLiveData.postValue(repo.getRockets())
        }
    }
}
