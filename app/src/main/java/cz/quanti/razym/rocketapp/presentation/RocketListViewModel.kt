package cz.quanti.razym.rocketapp.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cz.quanti.razym.rocketapp.data.RocketsRepoImpl
import cz.quanti.razym.rocketapp.domain.RocketModel
import cz.quanti.razym.rocketapp.domain.RocketsRepository

class RocketListViewModel(
    repo: RocketsRepository
)  : ViewModel() {
    val rocketLiveData: LiveData<List<RocketModel>> = MutableLiveData(repo.getRockets())

    class RocketListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RocketListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RocketListViewModel(
                    // TODO now dummy new instance, will be replaced by Koin soon.
                    // TODO Check if context is necessary then.
                    RocketsRepoImpl()
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
