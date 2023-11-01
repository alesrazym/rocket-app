package cz.quanti.razym.rocketapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.model.Rocket

class RocketListViewModel(
    repo: RocketsRepository
)  : ViewModel() {
    val rocketLiveData: LiveData<List<Rocket>> = MutableLiveData(repo.getRockets())
}
