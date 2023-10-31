package cz.quanti.razym.rocketapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cz.quanti.razym.rocketapp.domain.RocketModel
import cz.quanti.razym.rocketapp.domain.RocketsRepository

class RocketListViewModel(
    repo: RocketsRepository
)  : ViewModel() {
    val rocketLiveData: LiveData<List<RocketModel>> = liveData { emit(repo.getRockets()) }
}
