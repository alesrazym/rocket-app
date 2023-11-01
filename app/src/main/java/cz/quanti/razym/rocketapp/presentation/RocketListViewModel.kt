package cz.quanti.razym.rocketapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cz.quanti.razym.rocketapp.data.RocketsRepositoryImpl
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.model.RocketModel

class RocketListViewModel(
    repo: RocketsRepository
)  : ViewModel() {
    val rocketLiveData: LiveData<List<RocketModel>> = MutableLiveData(repo.getRockets())
}
